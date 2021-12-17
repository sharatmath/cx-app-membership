package com.dev.prepaid.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dev.prepaid.InitData;
import com.dev.prepaid.domain.PrepaidCxOfferConfig;
import com.dev.prepaid.model.imports.DataImportDTO;
import com.dev.prepaid.repository.PrepaidCxOfferConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dev.prepaid.model.invocation.DataExportDTO;
import com.dev.prepaid.model.invocation.DataSet;
import com.dev.prepaid.model.invocation.InstanceContext;
import com.dev.prepaid.model.invocation.InvocationRequest;
import com.dev.prepaid.model.invocation.ProductExpImpEndpoint;
import com.dev.prepaid.util.GsonUtils;
import com.dev.prepaid.util.JwtTokenUtil;
import com.dev.prepaid.util.RESTUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class RetryableServiceImpl implements RetryableService {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private PrepaidCxOfferConfigRepository prepaidCxOfferConfigRepository;

	@SuppressWarnings("unchecked")
	@Override
	public DataSet exportPage(InvocationRequest invocation, ProductExpImpEndpoint productExportEndpoint, 
			Integer exportPageSize, int pageNumber) throws Exception {
		log.debug("try {exportPage()}");
		try {
			
			String newToken = jwtTokenUtil.generateTokenExportProduct(invocation, invocation.getInstanceContext());
			
			//call rest api
			InstanceContext instanceContext = invocation.getInstanceContext();
			
			String url = productExportEndpoint.getUrl() + "?offset=" + (pageNumber * exportPageSize) + "&limit=" + exportPageSize;
			ResponseEntity<String> responseEntity = RESTUtil.getExportData(invocation, newToken, url, null, String.class, "application/json");
			
//			log.debug(responseEntity.getBody().toString());
			
			int statusCode = responseEntity.getStatusCodeValue();
			if (statusCode >= 400) {
				String msg = "Calling Product Export Endpoint: " + productExportEndpoint.getUrl()
				+ " resulted in an error status: " + statusCode;
				log.error(msg);
				throw new RuntimeException(msg);
			}
			

			DataExportDTO data = (DataExportDTO) GsonUtils.serializeObjectFromJSON(responseEntity.getBody().toString(), DataExportDTO.class);
			List<List<String>> rows = data.getDataSet().getRows();
			
//			DataSet data = (DataSet) GsonUtils.serializeObjectFromJSON(responseEntity.getBody().toString(), DataSet.class);			
//			List<List<String>> rows = data.getRows();
			
//			log.info("Successful Product Export from " + productExportEndpoint.getUrl());
//			log.debug("Pulled rows[{}]", rows);
			
			return data.getDataSet();
			
//			return data;
			
		} catch (Exception e) {
			log.error("Dataset was not properly exported, error: {}", e.getMessage());
			throw e;
		}
	}

	@Override
	public String getBackendResponseFallback(Exception e) {
		// TODO Auto-generated method stub
		log.info("ERROR : {}",e.getMessage());
		return null;
	}

	@Override
	public void callProductOnCompletionCallbackEndpoint(InvocationRequest invocation) throws Exception {		

		ResponseEntity response = null;
		InstanceContext instanceContext = invocation.getInstanceContext();
		String token = jwtTokenUtil.generateTokenProduct(invocation, instanceContext);
		String url = invocation.getOnCompletionCallbackEndpoint().getUrl();
		
	    Map<String, Object> body = new HashMap<>();
	    body.put("status", "COMPLETED"); // {"status": "COMPLETED"}
	    
	    if(invocation.getOnCompletionCallbackEndpoint().getMethod().equalsIgnoreCase("PATCH")) {
	    	response = RESTUtil.onCompletionCallbackPatch(invocation, token, url, body, null, "application/json");
	    	log.debug("onCompletionCallbackPatch response : {}",response.getStatusCode());	    	
	    }
	    if(invocation.getOnCompletionCallbackEndpoint().getMethod().equalsIgnoreCase("POST")) {
	    	response = RESTUtil.onCompletionCallbackPost(invocation, token, url, body, null, "application/json");
	    	log.debug("onCompletionCallbackPost response : {}",response.getStatusCode());
	    }
		
	}

	@Override
	@Async
	public void callProductImportEndpoint(InvocationRequest invocation) throws Exception {
		log.debug("call Retry_productImportEndpoint");
//		Thread.sleep(60000);
		ResponseEntity response = null;
		InstanceContext instanceContext = invocation.getInstanceContext();
		String token = jwtTokenUtil.generateTokenProduct(invocation, instanceContext);
		String url = invocation.getProductImportEndpoint().getUrl();

		List<List<String>> rows = new ArrayList<List<String>>();
		PrepaidCxOfferConfig config =  prepaidCxOfferConfigRepository.findOneByInstanceIdAndDeletedDateIsNull(
				invocation.getInstanceContext().getInstanceId()
		);
		invocation.getDataSet().getRows().forEach(row -> {
			Map<String, Object> input = invocation.getInstanceContext().getRecordDefinition().translateInputRowToMap(row);
			Map<String, Object> output = invocation.getInstanceContext().getRecordDefinition().generateOutputRowAsNewMap(input);
			List<String> listOutput = new ArrayList<>();
			listOutput.add(0, output.get("appcloud_row_correlation_id").toString());
			listOutput.add(1, "success");
			listOutput.add(2, "");
			listOutput.add(3, config.getOverallOfferName());
			listOutput.add(4, "success");

//			List<String> listOutput = List.of(
//					output.get("appcloud_row_correlation_id").toString(), //appcloud_row_correlation_id
//					"success", //appcloud_row_status
//					"", //appcloud_row_errormessage
//					config.getOverallOfferName(), //overallOfferName
//					"success"); //STATUS
			rows.add(listOutput);
		});

		DataSet dataSet = DataSet.builder()
				.id(invocation.getDataSet().getId())
				.rows(rows)
				.size(null)
				.build();

		DataImportDTO data = DataImportDTO.builder()
				.fieldDefinitions(InitData.recordDefinition.getOutputParameters())
				.dataSet(dataSet)
				.build();

		response = RESTUtil.productImportPost(invocation, token, url, data, null, "application/json");
		log.debug("productImportPost response : {}", response.getStatusCode());
	}

	
}
