package com.dev.prepaid.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dev.prepaid.repository.PrepaidOfferMembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.prepaid.constant.Constant;
import com.dev.prepaid.domain.PrepaidCxProvInstances;
import com.dev.prepaid.domain.PrepaidDaOfferBucket;
import com.dev.prepaid.domain.PrepaidDaOfferCampaign;
import com.dev.prepaid.domain.PrepaidOmsOfferBucket;
import com.dev.prepaid.domain.PrepaidOmsOfferCampaign;
import com.dev.prepaid.domain.PrepaidCxProvInvocations;
import com.dev.prepaid.model.CxAppsInfo;
import com.dev.prepaid.model.DataRowDTO;
import com.dev.prepaid.model.DataSetDTO;
import com.dev.prepaid.model.invocation.InstanceContext;
import com.dev.prepaid.model.invocation.InvocationRequest;
import com.dev.prepaid.repository.PrepaidCxProvInvocationsRepository;
import com.dev.prepaid.util.BaseRabbitTemplate;
import com.dev.prepaid.util.DateUtil;
import com.dev.prepaid.util.GsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PrepaidAppServiceImpl extends BaseRabbitTemplate implements PrepaidAppService{
	
	static final String DATE_FORMAT="yyyyMMdd";
	static final String DATETIME_FORMAT="yyyyMMdd'|'HHmmss";
	
	@Autowired
	private OfferService offerService;

	@Autowired
	PrepaidOfferMembershipRepository prepaidOfferMembershipRepository;
	@Autowired
	private PrepaidCxProvInvocationsRepository prepaidCxProvInvocationsRepository;
	

	@Override
	public List<DataRowDTO> processData(
			List<List<String>> rows, 
			InvocationRequest invocation, 
			PrepaidCxProvInstances instanceConfiguration,
			String groupId,
			Long dataSetSize) {
		
		List<DataRowDTO> processedRows = new ArrayList<>();
		CxAppsInfo cxAppsInfo = CxAppsInfo.builder()
				.invocationId(invocation.getUuid())
				.groupId(groupId)
				.productImportEndpoint(invocation.getProductImportEndpoint().getUrl())
				.productOnCompletionCallbackEndpoint(invocation.getOnCompletionCallbackEndpoint().getUrl())
				.dataSetSize(dataSetSize)
				.iss(invocation.getInstanceContext().getAppId())
				.sub(invocation.getInstanceContext().getInstallId())
				.aud(invocation.getInstanceContext().getInstanceId())
				.tenantId(invocation.getInstanceContext().getTenantId())
				.secret(invocation.getInstanceContext().getSecret())
				.build();
		
		// Process a page of data
		rows.forEach(row -> {
			Map<String, Object> input = invocation.getInstanceContext().getRecordDefinition().translateInputRowToMap(row);
//			log.debug("== INPUT ==");
//		    input.forEach((key, value) -> { log.debug(key +" : "+ value); });
			Map<String, Object> output = invocation.getInstanceContext().getRecordDefinition().generateOutputRowAsNewMap(input);
//			log.debug("== OUTPUT ==");
//		    output.forEach((key, value) -> { log.debug(key +" : "+ value); });		      
			DataRowDTO processedRow = processRow(invocation.getInstanceContext(), null, instanceConfiguration, input, output, cxAppsInfo);
			processedRows.add(processedRow);
		});
		
		DataSetDTO dataSetDto = new DataSetDTO();
		dataSetDto.setId(invocation.getDataSet().getId());
		dataSetDto.setRows(processedRows);
		dataSetDto.setSize(Long.valueOf(processedRows.size()));
		
		// Import a page of data
		// if (invocation.getProductImportEndpoint() != null) {
		//			importDataToProductImportEndpoint(invocation, dataSetDto);
		// }
		
		//finish
		
		
		try {
			PrepaidCxProvInvocations prepaidCxProvInvocations = prepaidCxProvInvocationsRepository.findOneById(invocation.getUuid());
			prepaidCxProvInvocations.setStatus("COMPLETED");
			
			if(!instanceConfiguration.getNotification()) {
				prepaidCxProvInvocationsRepository.save(prepaidCxProvInvocations);
			}
			
		} catch (Exception e) {
			log.info("ERROR : {}",e.getMessage());
		}
		
		return processedRows;
	}
	
	// send to PGS
	private DataRowDTO processRow(InstanceContext instanceContext, String nul, PrepaidCxProvInstances instanceConfiguration, 
			Map<String, Object> input, Map<String, Object> output, CxAppsInfo cxAppsInfo) {
		
		DataRowDTO processedRow = null;
		
		try {
			String requestId = (String) input.get("appcloud_row_correlation_id");//GUIDUtil.generateGUID();
			String msisdn = (String) input.get("CUSTOMER_ID_");//(String) input.get("MSISDN");
			Long campaignOfferId = instanceConfiguration.getCampaignOfferId();
			String campaignOfferType = instanceConfiguration.getCampaignOfferType();

			String cxAppsInfoJson		= GsonUtils.deserializeObjectToJSON(cxAppsInfo);
			Boolean notification		= instanceConfiguration.getNotification();
			
			if(campaignOfferType.equalsIgnoreCase("OMS")) {
				PrepaidOmsOfferCampaign prepaidOmsOfferCampaign = offerService.getOmsOfferCampaign(campaignOfferId);
				PrepaidOmsOfferBucket prepaidOmsOfferBucket = offerService.getOmsOfferBucket(prepaidOmsOfferCampaign.getOfferId());

				String offerId 				= prepaidOmsOfferBucket.getCode(); //prepaidOmsOfferCampaign.getId().toString(); // long to string  
				String offerType			= prepaidOmsOfferBucket.getType();
				Integer validityValue 		= (int) Math.round(prepaidOmsOfferCampaign.getValue()); // double to integer  
				String validityUnit			= prepaidOmsOfferCampaign.getValueUnit();
				String startDatetime		= null;
				String endDatetime			= null;
				String counterId			= prepaidOmsOfferCampaign.getCounterId();
				Integer counterValue		= 0; // We will need to default the counter value to 0 in the PGS API
				String thresholdId			= prepaidOmsOfferCampaign.getThresholdId();
				Integer thresholdValue		= Math.toIntExact(prepaidOmsOfferCampaign.getThresholdValue()); //long to integer
				String thresholdValueUnit	= prepaidOmsOfferCampaign.getThresholdValueUnit();
				String omsAction			= prepaidOmsOfferCampaign.getAction();
				String bizdate				= DateUtil.dateToString(new Date(), DATE_FORMAT); // date to dateformat
				String responsysCustomEvent	= "";
				
				if(prepaidOmsOfferBucket.getType().equalsIgnoreCase("account-based")) {
					startDatetime	= DateUtil.dateToString(prepaidOmsOfferCampaign.getStartDate(), DATE_FORMAT); // date to dateformat
					endDatetime		= DateUtil.dateToString(prepaidOmsOfferCampaign.getEndDate(), DATE_FORMAT); // date to dateformat
					
					//PGS
					Map<String, Object> requestPGS = new HashMap<>();
					requestPGS.put("method", "accountBased");
					
					requestPGS.put("requestId", requestId);
					requestPGS.put("msisdn", msisdn);        
					requestPGS.put("offerId", offerId); 													     
					requestPGS.put("offerType", offerType);        
					requestPGS.put("validityValue", validityValue); 							 
					requestPGS.put("validityUnit", validityUnit);
					requestPGS.put("startDatetime", startDatetime); 	
					requestPGS.put("endDatetime", endDatetime);  		    
					requestPGS.put("counterId", counterId);
					requestPGS.put("counterValue", counterValue);
					requestPGS.put("thresholdId", thresholdId);
					requestPGS.put("thresholdValue", thresholdValue); 				
					requestPGS.put("thresholdValueUnit", thresholdValueUnit);
					requestPGS.put("omsAction", omsAction);
					requestPGS.put("bizdate", bizdate);
					requestPGS.put("responsysCustomEvent", responsysCustomEvent);
					
					requestPGS.put("cxAppsInfoJson", cxAppsInfoJson);
					requestPGS.put("notification", notification);

					
					//send to PGS Queue
//					rabbitTemplateConvertAndSendWithPriority(Constant.TOPIC_EXCHANGE_NAME_SINGTEL,
//							Constant.QUEUE_NAME_SINGTEL_PGS,
//							requestPGS,
//							0);
					
//					processedRow.setRows(List.of(msisdn));
					
				}else if(prepaidOmsOfferBucket.getType().equalsIgnoreCase("time-based")){
					startDatetime	= DateUtil.dateToString(prepaidOmsOfferCampaign.getStartDate(), DATETIME_FORMAT); // date to datetimeformat
					endDatetime		= DateUtil.dateToString(prepaidOmsOfferCampaign.getEndDate(), DATETIME_FORMAT); // date to datetimeformat
					
					//PGS
					Map<String, Object> requestPGS = new HashMap<>();
					requestPGS.put("method", "timeBased");
					
					requestPGS.put("requestId", requestId);
					requestPGS.put("msisdn", msisdn);        
					requestPGS.put("offerId", offerId); 													     
					requestPGS.put("offerType", offerType);        
					requestPGS.put("validityValue", validityValue); 							 
					requestPGS.put("validityUnit", validityUnit);
					requestPGS.put("startDatetime", startDatetime); 	
					requestPGS.put("endDatetime", endDatetime);  		    
					requestPGS.put("counterId", counterId);
					requestPGS.put("counterValue", counterValue);
					requestPGS.put("thresholdId", thresholdId);
					requestPGS.put("thresholdValue", thresholdValue); 				
					requestPGS.put("thresholdValueUnit", thresholdValueUnit);
					requestPGS.put("omsAction", omsAction);
					requestPGS.put("bizdate", bizdate);
					requestPGS.put("responsysCustomEvent", responsysCustomEvent);
					
					requestPGS.put("cxAppsInfoJson", cxAppsInfoJson);
					requestPGS.put("notification", notification);
					
					//send to PGS Queue		
//					rabbitTemplateConvertAndSendWithPriority(Constant.TOPIC_EXCHANGE_NAME_SINGTEL,
//							Constant.QUEUE_NAME_SINGTEL_PGS,
//							requestPGS,
//							0);
					
//					processedRow.setRows(List.of(msisdn));
				}
			}
			if(campaignOfferType.equalsIgnoreCase("DA")) {
				PrepaidDaOfferCampaign prepaidDaOfferCampaign = offerService.getDaOfferCampaign(campaignOfferId);
				PrepaidDaOfferBucket prepaidDaOfferBucket = offerService.getDaOfferBucket(prepaidDaOfferCampaign.getOfferId());
				
				String dedaccid 			= prepaidDaOfferBucket.getCode(); // CODE
				String daExpiry				= prepaidDaOfferCampaign.getValueValidityInDays().toString(); //VALUE_VALIDITY_IN_DAYS
				String maDebitAmount		= String.valueOf(Math.round(prepaidDaOfferCampaign.getValueToDeductFromMa())); //VALUE_TO_DEDUCT_FROM_MA				
				String daAction				= prepaidDaOfferCampaign.getAction();
				String bizdate				= DateUtil.dateToString(new Date(), DATE_FORMAT); // date to dateformat
				String responsysCustomEvent	= "";
				String validity				= prepaidDaOfferCampaign.getValueValidityInDays().toString();
				
				Double value = prepaidDaOfferCampaign.getValue();
				if(prepaidDaOfferCampaign.getValueUnit().equalsIgnoreCase("AMOUNT")) value = value * 100;
				if(prepaidDaOfferCampaign.getValueUnit().equalsIgnoreCase("MB")) value = value * 553;
				
				//requestId, mobile, value, dedaccid, daAction,validity, maDebitAmount, bizDate, cxAppsInfoJson, notification
				//PVAS
				Map<String, Object> requestPVAS = new HashMap<>();
				requestPVAS.put("requestId", requestId);
				requestPVAS.put("mobile", msisdn);
				requestPVAS.put("value", String.valueOf(Math.round(value))); //VALUE (tergantung unit ubah ke cents)
//				requestPVAS.put("daExpiry", daExpiry);
				requestPVAS.put("validity", validity);
				requestPVAS.put("dedaccid", dedaccid);
				requestPVAS.put("maDebitAmount", maDebitAmount); 
				requestPVAS.put("daAction", daAction);
				requestPVAS.put("bizdate", bizdate);
				requestPVAS.put("responsysCustomEvent", responsysCustomEvent);
				
				requestPVAS.put("cxAppsInfoJson", cxAppsInfoJson);
				requestPVAS.put("notification", notification);
				
				//send to PVAS Queue
//				rabbitTemplateConvertAndSendWithPriority(Constant.TOPIC_EXCHANGE_NAME_SINGTEL,
//						Constant.QUEUE_NAME_SINGTEL_PVAS,
//						requestPVAS,
//						0);
				
//				processedRow.setRows((ArrayList<String>) List.of(msisdn));
			}
					

		} catch (Exception e) {
			log.info("ERROR : {}",e.getMessage());
		}
		return processedRow;
	}

}
