package com.dev.prepaid.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.dev.prepaid.domain.*;
import com.dev.prepaid.model.configuration.OfferPromoCode;
import com.dev.prepaid.model.configuration.OfferSelection;
import com.dev.prepaid.repository.*;
import com.dev.prepaid.type.OfferType;
import com.dev.prepaid.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import oracle.ucp.proxy.annotation.Pre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.prepaid.InitData;
import com.dev.prepaid.model.configuration.SaveConfigRequest;
import com.dev.prepaid.model.create.ServiceInstance;
import com.dev.prepaid.model.install.AppInstall;
import com.dev.prepaid.util.GUIDUtil;
import com.dev.prepaid.util.GsonUtils;
@Slf4j
@Service
public class PrepaidCxServiceImpl implements PrepaidCxService {

	@Autowired
	private PrepaidCxProvApplicationRepository prepaidCxProvApplicationRepository;

	@Autowired
	private PrepaidCxOfferConfigRepository prepaidCxOfferConfigRepository;

	@Autowired
	private PrepaidCxOfferSelectionRepository prepaidCxOfferSelectionRepository;

	@Autowired
	private PrepaidCxOfferEligibilityRepository prepaidCxOfferEligibilityRepository;

	@Autowired
	private PrepaidCxOfferMonitoringRepository prepaidCxOfferMonitoringRepository;

	@Autowired
	private PrepaidCxOfferRedemptionRepository prepaidCxOfferRedemptionRepository;

	@Autowired
	private PrepaidCxOfferEventConditionRepository prepaidCxOfferEventConditionRepository;

	@Override
	public void appInstallAddEntity(AppInstall appInstall) {
		
		PrepaidCxProvApplication prepaidCxProvApplication = prepaidCxProvApplicationRepository
				.findOneByInstallIdAndAppIdAndUninstallDateIsNull
				(appInstall.getApplicationInstall().getUuid(), 
				 appInstall.getApplicationInstall().getApplication().getUuid());
		
		if(prepaidCxProvApplication == null) {
			prepaidCxProvApplication = new PrepaidCxProvApplication().builder()
					.id(GUIDUtil.generateGUID())
					
					.installId(appInstall.getApplicationInstall().getUuid())
					.appId(appInstall.getApplicationInstall().getApplication().getUuid())
					
					.createdBy(appInstall.getApplicationInstall().getApplication().getName())
					.createdDate(new Date())
					.build();
			
			prepaidCxProvApplicationRepository.save(prepaidCxProvApplication);
		}
		
	}

	@Override
	public void appCreateAddEntity(ServiceInstance serviceInstance) {
		log.debug("getInstallUuid {}  getUuid {}",
				serviceInstance.getApplicationServiceInstall().getInstallUuid(),
				serviceInstance.getApplicationServiceInstall().getApplication().getUuid());
		PrepaidCxProvApplication prepaidCxProvApplication = prepaidCxProvApplicationRepository
				.findOneByInstallIdAndAppIdAndUninstallDateIsNull
				(serviceInstance.getApplicationServiceInstall().getInstallUuid(), 
				 serviceInstance.getApplicationServiceInstall().getApplication().getUuid());
		log.debug("prepaidCxProvApplication {} ", prepaidCxProvApplication );
		if(prepaidCxProvApplication == null) return; //return or throw exception

		Optional<PrepaidCxOfferConfig> opsFind = prepaidCxOfferConfigRepository.findByInstanceId(
				serviceInstance.getUuid());
		if(!opsFind.isPresent()){
			String offerConfigId = GUIDUtil.generateGUID();
			PrepaidCxOfferConfig prepaidCxOfferConfig = PrepaidCxOfferConfig.builder()
					.id(offerConfigId)
					.instanceId(serviceInstance.getUuid())
					.programId(serviceInstance.getAssetId())
//					.programName(serviceInstance.getApplicationServiceInstall().getName())
					.provisionType(serviceInstance.getAssetType())
					.build();

			prepaidCxOfferConfigRepository.save(prepaidCxOfferConfig);
		}
	}
	
	@Override
	public void appConfigurationAddEntity(SaveConfigRequest saveConfigRequest) {
		Boolean notification = (saveConfigRequest.getPayload().getNotification() != null) ? saveConfigRequest.getPayload().getNotification() : false;
		// TODO update entity
//		Optional<PrepaidCxOfferConfig> opsFind = prepaidCxOfferConfigRepository.findByInstanceId(saveConfigRequest.getInstanceUuid());
		Optional<PrepaidCxOfferConfig> opsFind = prepaidCxOfferConfigRepository.findByInstanceId(saveConfigRequest.getPayload().getUuid());

		PrepaidCxOfferConfig prepaidCxOfferConfig = null;
		log.debug("get data  {} ", opsFind);
		if(opsFind.isPresent()) {
			prepaidCxOfferConfig = opsFind.get();
			opsFind.get().setProvisionType(saveConfigRequest.getPayload().getType());
			if(prepaidCxOfferConfig.getId() != null) {
				if (saveConfigRequest.getPayload().getOfferPromoCode() != null) {
					opsFind.get().setOverallOfferName(saveConfigRequest.getPayload().getOfferPromoCode().getOverallOfferName());
				}
			}
			prepaidCxOfferConfig = prepaidCxOfferConfigRepository.save(opsFind.get());

			if(prepaidCxOfferConfig.getId() != null){
				try {
					if (saveConfigRequest.getPayload().getOfferSelection() != null) {
						saveOfferSelection(prepaidCxOfferConfig.getId(), saveConfigRequest);
					}
					if (saveConfigRequest.getPayload().getOfferRedemption() != null) {
						saveOfferRedemption(prepaidCxOfferConfig.getId(), saveConfigRequest);
					}
					if (saveConfigRequest.getPayload().getOfferEligibility() != null) {
						saveOfferEligibility(prepaidCxOfferConfig.getId(), saveConfigRequest);
					}
					if (saveConfigRequest.getPayload().getOfferMonitoring() != null) {
						saveOfferMonitoring(prepaidCxOfferConfig.getId(), saveConfigRequest);
					}
					if (saveConfigRequest.getPayload().getOfferEventCondition() != null) {
						saveOfferEventCondition(prepaidCxOfferConfig.getId(), saveConfigRequest);
					}
				}catch (Exception ex){
					log.error("", ex);
				}
			}
		}
		
	}

	@Override
	public void appDeleteEntity(String instanceUuid) {
		PrepaidCxOfferConfig prepaidCxOfferConfig = prepaidCxOfferConfigRepository.
				findOneByInstanceIdAndDeletedDateIsNull(instanceUuid);

		if(prepaidCxOfferConfig != null) {
			prepaidCxOfferConfig.setDeletedBy("-");
			prepaidCxOfferConfig.setDeletedDate(new Date());

			prepaidCxOfferConfigRepository.save(prepaidCxOfferConfig);
		}
	}

	@Override
	public void appUninstallEntity(String installId, String appId) {
		PrepaidCxProvApplication prepaidCxProvApplication = prepaidCxProvApplicationRepository
				.findOneByInstallIdAndAppIdAndUninstallDateIsNull(installId, appId);
		
		prepaidCxProvApplication.setUninstallBy("-");
		prepaidCxProvApplication.setUninstallDate(new Date());
		
		prepaidCxProvApplicationRepository.save(prepaidCxProvApplication);		
	}

	private void saveOfferEligibility(String offerConfigId, SaveConfigRequest saveConfigRequest){
		PrepaidCxOfferEligibility prepaidCxOfferEligibility;
		Optional<PrepaidCxOfferEligibility> optionalPrepaidCxOfferEligibility =  prepaidCxOfferEligibilityRepository.findByOfferConfigId(offerConfigId);

		if(optionalPrepaidCxOfferEligibility.isPresent()){
			prepaidCxOfferEligibility = optionalPrepaidCxOfferEligibility.get();

		}else {
			prepaidCxOfferEligibility = PrepaidCxOfferEligibility.builder()
					.offerConfigId(offerConfigId)
					.build();
		}
		log.info("current {}",prepaidCxOfferEligibility);

		prepaidCxOfferEligibility.setIsFrequencyOnly(saveConfigRequest.getPayload().getOfferEligibility().getIsFrequencyOnly());
		prepaidCxOfferEligibility.setFrequency(saveConfigRequest.getPayload().getOfferEligibility().getFrequency());
		prepaidCxOfferEligibility.setIsFrequencyAndTime(saveConfigRequest.getPayload().getOfferEligibility().getIsFrequencyAndTime());
		prepaidCxOfferEligibility.setNumberOfFrequency(saveConfigRequest.getPayload().getOfferEligibility().getNumberOfFrequency());
		prepaidCxOfferEligibility.setNumberOfDays(saveConfigRequest.getPayload().getOfferEligibility().getNumberOfDays());
		prepaidCxOfferEligibility.setIsOfferLevelCapOnly(saveConfigRequest.getPayload().getOfferEligibility().getIsOfferLevelCapOnly());
		prepaidCxOfferEligibility.setOfferLevelCapValue(saveConfigRequest.getPayload().getOfferEligibility().getOfferLevelCapValue());
		prepaidCxOfferEligibility.setIsOfferLevelCapAndPeriod(saveConfigRequest.getPayload().getOfferEligibility().getIsOfferLevelCapAndPeriod());
		prepaidCxOfferEligibility.setOfferLevelCapPeriodValue(saveConfigRequest.getPayload().getOfferEligibility().getOfferLevelCapPeriodValue());
		prepaidCxOfferEligibility.setOfferLevelCapPeriodDays(saveConfigRequest.getPayload().getOfferEligibility().getOfferLevelCapPeriodDays());
		prepaidCxOfferEligibility.setExcludeProgramId(saveConfigRequest.getPayload().getOfferEligibility().getExcludeProgramId());
		prepaidCxOfferEligibilityRepository.save(prepaidCxOfferEligibility);
	}

	private void saveOfferSelection(String offerConfigId, SaveConfigRequest saveConfigRequest){
		log.info("saveOfferSelection {}", saveConfigRequest.getPayload().getOfferSelection());
		List<PrepaidCxOfferSelection> currents = prepaidCxOfferSelectionRepository.findByOfferConfigId(offerConfigId);
		for(PrepaidCxOfferSelection c : currents){
			prepaidCxOfferSelectionRepository.deleteById(c.getId());
		}
		// PROMO
		if(saveConfigRequest.getPayload().getOfferPromoCode() != null) {
			OfferPromoCode promoCode = saveConfigRequest.getPayload().getOfferPromoCode();
			Optional<PrepaidCxOfferSelection> opsFind = prepaidCxOfferSelectionRepository.findByOfferConfigIdAndOfferBucketTypeAndOfferBucketIdAndOfferId(
					offerConfigId,
					OfferType.PROMO.toString(),
					"0",
					String.valueOf(0)
			);
			log.info("offerSelection PROMO {} {}", opsFind, promoCode);
			PrepaidCxOfferSelection prepaidCxOfferSelection;
			if (opsFind.isPresent()) {
				prepaidCxOfferSelection = opsFind.get();
				prepaidCxOfferSelection.setSmsCampaignName(promoCode.getSmsCampaignName());
				prepaidCxOfferSelection.setPromoCodeList(promoCode.getPromoCodeList());
				prepaidCxOfferSelection.setMessageText1(promoCode.getMessageText1());
				prepaidCxOfferSelection.setMessageText2(promoCode.getMessageText2());
				prepaidCxOfferSelection.setMessageText3(promoCode.getMessageText3());
				prepaidCxOfferSelection.setMessageText4(promoCode.getMessageText4());
			} else {
				prepaidCxOfferSelection = PrepaidCxOfferSelection.builder()
						.offerConfigId(offerConfigId)
						.offerBucketType(OfferType.PROMO.toString())
						.offerBucketId("0")
						.offerId("0")
						.smsCampaignName(promoCode.getSmsCampaignName())
						.promoCodeList(promoCode.getPromoCodeList())
						.messageText1(promoCode.getMessageText1())
						.messageText2(promoCode.getMessageText2())
						.messageText3(promoCode.getMessageText3())
						.messageText4(promoCode.getMessageText4())
						.build();
			}
			prepaidCxOfferSelectionRepository.save(prepaidCxOfferSelection);
		}
		//OMS & DA
		for(OfferSelection offerSelection: saveConfigRequest.getPayload().getOfferSelection()){
			Optional<PrepaidCxOfferSelection> opsFind = prepaidCxOfferSelectionRepository.findByOfferConfigIdAndOfferBucketTypeAndOfferBucketIdAndOfferId(
					offerConfigId,
					offerSelection.getOfferBucketType(),
					offerSelection.getOfferBucketId(),
					String.valueOf(offerSelection.getOfferCampaignId())
			);
			log.info("offerSelection {} {}", opsFind, offerSelection);
			PrepaidCxOfferSelection prepaidCxOfferSelection;
			if(opsFind.isPresent()){
				prepaidCxOfferSelection = opsFind.get();
				prepaidCxOfferSelection.setOfferBucketId(offerSelection.getOfferBucketId());
				prepaidCxOfferSelection.setOfferType(offerSelection.getOfferCampaignName());
				prepaidCxOfferSelection.setOfferId(String.valueOf(offerSelection.getOfferCampaignId()));
			}else {
				prepaidCxOfferSelection = PrepaidCxOfferSelection.builder()
						.offerConfigId(offerConfigId)
						.offerBucketType(offerSelection.getOfferBucketType())
						.offerBucketId(offerSelection.getOfferBucketId())
						.offerType(offerSelection.getOfferCampaignName())
						.offerId(String.valueOf(offerSelection.getOfferCampaignId()))
						.build();
			}
			prepaidCxOfferSelectionRepository.save(prepaidCxOfferSelection);
		}
	}

	private void saveOfferMonitoring(String offerConfigId, SaveConfigRequest saveConfigRequest) {
		log.info("saveOfferMonitoring {}", saveConfigRequest.getPayload().getOfferMonitoring());
		Optional<PrepaidCxOfferMonitoring> opsFind = prepaidCxOfferMonitoringRepository.findByOfferConfigId(offerConfigId);
		PrepaidCxOfferMonitoring prepaidCxOfferMonitoring;
		if (opsFind.isPresent()) {
			prepaidCxOfferMonitoring = opsFind.get();
		}else {
			prepaidCxOfferMonitoring = PrepaidCxOfferMonitoring.builder()
					.offerConfigId(offerConfigId)
					.build();
		}
		prepaidCxOfferMonitoring.setEventType(saveConfigRequest.getPayload().getOfferMonitoring().getEventType());
		//monitoring
		boolean isMonitorSpecificPeriod =
				saveConfigRequest.getPayload().getOfferMonitoring().getMonitorSpecifiedPeriodRadio()  == null ? false :
						saveConfigRequest.getPayload().getOfferMonitoring().getMonitorSpecifiedPeriodRadio();

		boolean isMonitorDateRange = saveConfigRequest.getPayload().getOfferMonitoring().getMonitorPeriodRadio() == null ? false :
						saveConfigRequest.getPayload().getOfferMonitoring().getMonitorPeriodRadio();

		if(isMonitorSpecificPeriod) {
			prepaidCxOfferMonitoring.setIsMonitorSpecificPeriod(saveConfigRequest.getPayload().getOfferMonitoring().getMonitorSpecifiedPeriodRadio());
			prepaidCxOfferMonitoring.setPeriodEndDate(DateUtil.stringToDate(saveConfigRequest.getPayload().getOfferMonitoring().getMonitorStartDate()));
			prepaidCxOfferMonitoring.setPeriodStartDate(DateUtil.stringToDate(saveConfigRequest.getPayload().getOfferMonitoring().getMonitorEndDate()));
		}
		if(isMonitorDateRange) {
			prepaidCxOfferMonitoring.setIsMonitorDateRange(saveConfigRequest.getPayload().getOfferMonitoring().getMonitorPeriodRadio());
			prepaidCxOfferMonitoring.setPeriodDays(saveConfigRequest.getPayload().getOfferMonitoring().getMonitorPeriod());
			prepaidCxOfferMonitoring.setPeriod(saveConfigRequest.getPayload().getOfferMonitoring().getMonitorPeriodDayMonth());
		}

		if("Top-Up".equals(prepaidCxOfferMonitoring.getEventType())){
			prepaidCxOfferMonitoring.setCreditMethod(saveConfigRequest.getPayload().getOfferMonitoring().getTopUpCreditMethod());
			prepaidCxOfferMonitoring.setUsageServiceType(saveConfigRequest.getPayload().getOfferMonitoring().getTopUpUsageServiceType());
			prepaidCxOfferMonitoring.setOperatorId(saveConfigRequest.getPayload().getOfferMonitoring().getTopUpOperator());
			prepaidCxOfferMonitoring.setTopUpCode(saveConfigRequest.getPayload().getOfferMonitoring().getTopUpCode());

			prepaidCxOfferMonitoring.setTopUpCurBalanceOp(saveConfigRequest.getPayload().getOfferMonitoring().getTopUpCurBalanceOp());
			if(saveConfigRequest.getPayload().getOfferMonitoring().getTopUpTransactionValue() != null) {
				prepaidCxOfferMonitoring.setTopUpCurBalanceValue(Long.valueOf(saveConfigRequest.getPayload().getOfferMonitoring().getTopUpTransactionValue()));
			}
			prepaidCxOfferMonitoring.setTopUpAccBalanceBeforeOp(saveConfigRequest.getPayload().getOfferMonitoring().getTopUpAccBalanceBeforeOp());
			prepaidCxOfferMonitoring.setTopUpAccBalanceBeforeValue(saveConfigRequest.getPayload().getOfferMonitoring().getTopUpAccBalanceBeforeValue());
			prepaidCxOfferMonitoring.setTopUpOp(saveConfigRequest.getPayload().getOfferMonitoring().getTopUpOp());
			prepaidCxOfferMonitoring.setTopUpTransactionValue(saveConfigRequest.getPayload().getOfferMonitoring().getTopUpTransactionValue());
			prepaidCxOfferMonitoring.setTopUpDaId(saveConfigRequest.getPayload().getOfferMonitoring().getTopUpDaId());
			prepaidCxOfferMonitoring.setTopUpDaBalanceOp(saveConfigRequest.getPayload().getOfferMonitoring().getTopUpDaBalanceOp());
			prepaidCxOfferMonitoring.setTopUpDaBalanceValue(saveConfigRequest.getPayload().getOfferMonitoring().getTopUpDaBalanceValue());
			prepaidCxOfferMonitoring.setTopUpTempServiceClass(saveConfigRequest.getPayload().getOfferMonitoring().getTopUpTempServiceClass());


		}else if("ARPU".equals(prepaidCxOfferMonitoring.getEventType())){
			prepaidCxOfferMonitoring.setOperatorId(saveConfigRequest.getPayload().getOfferMonitoring().getOperatorId());
			//arpu
			prepaidCxOfferMonitoring.setArpuType(saveConfigRequest.getPayload().getOfferMonitoring().getArpuType());
			prepaidCxOfferMonitoring.setArpuOp(saveConfigRequest.getPayload().getOfferMonitoring().getArpuOp());
			prepaidCxOfferMonitoring.setArpuValue(saveConfigRequest.getPayload().getOfferMonitoring().getArpuValue());
			prepaidCxOfferMonitoring.setArpuSelectedTopUpCode(saveConfigRequest.getPayload().getOfferMonitoring().getArpuSelectedTopUpCode());
		}else if("Usage".equals(prepaidCxOfferMonitoring.getEventType())){
			prepaidCxOfferMonitoring.setUsageServiceType(saveConfigRequest.getPayload().getOfferMonitoring().getUsageServiceType());
			prepaidCxOfferMonitoring.setUsageType(saveConfigRequest.getPayload().getOfferMonitoring().getUsageType());
			prepaidCxOfferMonitoring.setUsageOp(saveConfigRequest.getPayload().getOfferMonitoring().getUsageOperator());
			prepaidCxOfferMonitoring.setUsageValue(saveConfigRequest.getPayload().getOfferMonitoring().getUsageValue());
			prepaidCxOfferMonitoring.setCountryCode(saveConfigRequest.getPayload().getOfferMonitoring().getCountryCode());
		}

		prepaidCxOfferMonitoringRepository.save(prepaidCxOfferMonitoring);
	}

	private void saveOfferRedemption(String offerConfigId, SaveConfigRequest saveConfigRequest){
		log.info("saveOfferRedemption {}", saveConfigRequest.getPayload().getOfferRedemption());
		Optional<PrepaidCxOfferRedemption> opsFind = prepaidCxOfferRedemptionRepository.findByOfferConfigId(offerConfigId);
		PrepaidCxOfferRedemption prepaidCxOfferRedemption;
		if(opsFind.isPresent()){
			prepaidCxOfferRedemption = opsFind.get();
			prepaidCxOfferRedemption.setIsRedemptionCapOnly(saveConfigRequest.getPayload().getOfferRedemption().getIsRedemptionCapOnly());
			prepaidCxOfferRedemption.setRedemptionCapValue(saveConfigRequest.getPayload().getOfferRedemption().getRedemptionCapValue());
			prepaidCxOfferRedemption.setIsRedemptionCapAndPeriod(saveConfigRequest.getPayload().getOfferRedemption().getIsRedemptionCapAndPeriod());
			prepaidCxOfferRedemption.setTotalRedemptionPeriodValue(saveConfigRequest.getPayload().getOfferRedemption().getTotalRedemptionPeriodValue());
			prepaidCxOfferRedemption.setTotalRedemptionPeriodEvery(saveConfigRequest.getPayload().getOfferRedemption().getTotalRedemptionPeriodEvery());
			prepaidCxOfferRedemption.setIsFrequencyOnly(saveConfigRequest.getPayload().getOfferRedemption().getIsFrequencyOnly());
			prepaidCxOfferRedemption.setIsFrequencyAndTime(saveConfigRequest.getPayload().getOfferRedemption().getIsFrequencyAndTime());
			prepaidCxOfferRedemption.setFrequencyValue(saveConfigRequest.getPayload().getOfferRedemption().getFrequencyValue());
			prepaidCxOfferRedemption.setTimePeriodValue(saveConfigRequest.getPayload().getOfferRedemption().getTimePeriodValue());
			prepaidCxOfferRedemption.setTimePeriodType(saveConfigRequest.getPayload().getOfferRedemption().getTimePeriodType());
			prepaidCxOfferRedemption.setTimePeriodEvery(saveConfigRequest.getPayload().getOfferRedemption().getTimePeriodEvery());
			prepaidCxOfferRedemption.setTotalRedemptionPeriodType(saveConfigRequest.getPayload().getOfferRedemption().getTotalRedemptionPeriodType());
			prepaidCxOfferRedemption.setTotalRecurringFrequency(saveConfigRequest.getPayload().getOfferRedemption().getTotalRecurringFrequency());
			prepaidCxOfferRedemption.setIsRecurringFrequencyAndPeriod(saveConfigRequest.getPayload().getOfferRedemption().getIsRecurringFrequencyAndPeriod());
			prepaidCxOfferRedemption.setRecurringFrequencyValue(saveConfigRequest.getPayload().getOfferRedemption().getRecurringFrequencyValue());
			prepaidCxOfferRedemption.setRecurringFrequencyPeriodType(saveConfigRequest.getPayload().getOfferRedemption().getRecurringFrequencyPeriodType());
			prepaidCxOfferRedemption.setRecurringFrequencyPeriodValue(saveConfigRequest.getPayload().getOfferRedemption().getRecurringFrequencyPeriodValue());

			prepaidCxOfferRedemption.setIsRecurringFrequencyEachMonth(saveConfigRequest.getPayload().getOfferRedemption().getIsRecurringFrequencyEachMonth());
			prepaidCxOfferRedemption.setRecurringFrequencyDayOfMonth(saveConfigRequest.getPayload().getOfferRedemption().getRecurringFrequencyDayOfMonth());
			prepaidCxOfferRedemption.setRedemptionMethod(saveConfigRequest.getPayload().getOfferRedemption().getRedemptionMethod());
			prepaidCxOfferRedemption.setSmsKeyword(saveConfigRequest.getPayload().getOfferRedemption().getSmsKeyword());
			prepaidCxOfferRedemption.setSmsKeywordValidityDays(saveConfigRequest.getPayload().getOfferRedemption().getSmsKeywordValidityDays());
			prepaidCxOfferRedemption.setRecurringProvisioning(saveConfigRequest.getPayload().getOfferRedemption().isRecurringProvisioning());
			prepaidCxOfferRedemption.setSmsCampaignName(saveConfigRequest.getPayload().getOfferRedemption().getSmsCampaignName());
			prepaidCxOfferRedemption.setPostSmsCampaignName(saveConfigRequest.getPayload().getOfferRedemption().getPostSmsCampaignName());
			prepaidCxOfferRedemption.setDynamicVariable1(saveConfigRequest.getPayload().getOfferRedemption().getDynamicVariable1());
			prepaidCxOfferRedemption.setDynamicVariable2(saveConfigRequest.getPayload().getOfferRedemption().getDynamicVariable2());
			prepaidCxOfferRedemption.setDynamicVariable3(saveConfigRequest.getPayload().getOfferRedemption().getDynamicVariable3());
			prepaidCxOfferRedemption.setDynamicVariable4(saveConfigRequest.getPayload().getOfferRedemption().getDynamicVariable4());
			prepaidCxOfferRedemption.setDynamicVariable5(saveConfigRequest.getPayload().getOfferRedemption().getDynamicVariable5());

			prepaidCxOfferRedemption.setOptKeyword(saveConfigRequest.getPayload().getOfferRedemption().getOptKeyword());
			prepaidCxOfferRedemption.setDateRange(saveConfigRequest.getPayload().getOfferRedemption().isDateRange());
			if(saveConfigRequest.getPayload().getOfferRedemption().getOptStartDate() != null) {
				prepaidCxOfferRedemption.setOptStartDate(DateUtil.stringToDate(saveConfigRequest.getPayload().getOfferRedemption().getOptStartDate()));
			}
			if(saveConfigRequest.getPayload().getOfferRedemption().getOptEndDate() != null) {
				prepaidCxOfferRedemption.setOptEndDate(DateUtil.stringToDate(saveConfigRequest.getPayload().getOfferRedemption().getOptEndDate()));
			}
			prepaidCxOfferRedemption.setPeriod(saveConfigRequest.getPayload().getOfferRedemption().isPeriod());
			prepaidCxOfferRedemption.setOptPeriod(saveConfigRequest.getPayload().getOfferRedemption().getOptPeriod());

		}else {
			prepaidCxOfferRedemption = PrepaidCxOfferRedemption.builder()
					.offerConfigId(offerConfigId)
					.isRedemptionCapOnly(saveConfigRequest.getPayload().getOfferRedemption().getIsRedemptionCapOnly())
					.redemptionCapValue(saveConfigRequest.getPayload().getOfferRedemption().getRedemptionCapValue())
					.isRedemptionCapAndPeriod(saveConfigRequest.getPayload().getOfferRedemption().getIsRedemptionCapAndPeriod())
					.totalRedemptionPeriodValue(saveConfigRequest.getPayload().getOfferRedemption().getTotalRedemptionPeriodValue())
					.totalRedemptionPeriodEvery(saveConfigRequest.getPayload().getOfferRedemption().getTotalRedemptionPeriodEvery())
					.isFrequencyOnly(saveConfigRequest.getPayload().getOfferRedemption().getIsFrequencyOnly())
					.isFrequencyAndTime(saveConfigRequest.getPayload().getOfferRedemption().getIsFrequencyAndTime())
					.frequencyValue(saveConfigRequest.getPayload().getOfferRedemption().getFrequencyValue())
					.timePeriodValue(saveConfigRequest.getPayload().getOfferRedemption().getTimePeriodValue())
					.timePeriodType(saveConfigRequest.getPayload().getOfferRedemption().getTimePeriodType())
					.timePeriodEvery(saveConfigRequest.getPayload().getOfferRedemption().getTimePeriodEvery())
					.totalRedemptionPeriodType(saveConfigRequest.getPayload().getOfferRedemption().getTotalRedemptionPeriodType())
					.totalRecurringFrequency(saveConfigRequest.getPayload().getOfferRedemption().getTotalRecurringFrequency())
					.isRecurringFrequencyAndPeriod(saveConfigRequest.getPayload().getOfferRedemption().getIsRecurringFrequencyAndPeriod())
					.recurringFrequencyValue(saveConfigRequest.getPayload().getOfferRedemption().getRecurringFrequencyValue())
					.recurringFrequencyPeriodType(saveConfigRequest.getPayload().getOfferRedemption().getRecurringFrequencyPeriodType())
					.recurringFrequencyPeriodValue(saveConfigRequest.getPayload().getOfferRedemption().getRecurringFrequencyPeriodValue())
					.isRecurringFrequencyEachMonth(saveConfigRequest.getPayload().getOfferRedemption().getIsRecurringFrequencyEachMonth())
					.recurringFrequencyDayOfMonth(saveConfigRequest.getPayload().getOfferRedemption().getRecurringFrequencyDayOfMonth())
					.redemptionMethod(saveConfigRequest.getPayload().getOfferRedemption().getRedemptionMethod())
					.smsKeyword(saveConfigRequest.getPayload().getOfferRedemption().getSmsKeyword())
					.smsKeywordValidityDays(saveConfigRequest.getPayload().getOfferRedemption().getSmsKeywordValidityDays())
					.smsCampaignName(saveConfigRequest.getPayload().getOfferRedemption().getSmsCampaignName())
					.postSmsCampaignName(saveConfigRequest.getPayload().getOfferRedemption().getPostSmsCampaignName())
					.dynamicVariable1(saveConfigRequest.getPayload().getOfferRedemption().getDynamicVariable1())
					.dynamicVariable2(saveConfigRequest.getPayload().getOfferRedemption().getDynamicVariable2())
					.dynamicVariable3(saveConfigRequest.getPayload().getOfferRedemption().getDynamicVariable3())
					.dynamicVariable4(saveConfigRequest.getPayload().getOfferRedemption().getDynamicVariable4())
					.dynamicVariable5(saveConfigRequest.getPayload().getOfferRedemption().getDynamicVariable5())
					.isRecurringProvisioning(saveConfigRequest.getPayload().getOfferRedemption().isRecurringProvisioning())
					.build();

			prepaidCxOfferRedemption.setOptKeyword(saveConfigRequest.getPayload().getOfferRedemption().getOptKeyword());
			prepaidCxOfferRedemption.setDateRange(saveConfigRequest.getPayload().getOfferRedemption().isDateRange());
			if(saveConfigRequest.getPayload().getOfferRedemption().getOptStartDate() != null){
				prepaidCxOfferRedemption.setOptStartDate(DateUtil.stringToDate(saveConfigRequest.getPayload().getOfferRedemption().getOptStartDate()));
			}
			if(saveConfigRequest.getPayload().getOfferRedemption().getOptEndDate() != null) {
				prepaidCxOfferRedemption.setOptEndDate(DateUtil.stringToDate(saveConfigRequest.getPayload().getOfferRedemption().getOptEndDate()));
			}
			prepaidCxOfferRedemption.setPeriod(saveConfigRequest.getPayload().getOfferRedemption().isPeriod());
			prepaidCxOfferRedemption.setOptPeriod(saveConfigRequest.getPayload().getOfferRedemption().getOptPeriod());
		}
		prepaidCxOfferRedemptionRepository.save(prepaidCxOfferRedemption);
	}


	private void saveOfferEventCondition(String offerConfigId, SaveConfigRequest saveConfigRequest) {
		log.info("saveOfferEventCondition {}", saveConfigRequest.getPayload().getOfferEventCondition());
		Optional<PrepaidCxOfferEventCondition> opsFind = prepaidCxOfferEventConditionRepository.findByOfferConfigId(offerConfigId);
		PrepaidCxOfferEventCondition prepaidCxOfferEventCondition;
		if (opsFind.isPresent()) {
			prepaidCxOfferEventCondition = opsFind.get();
		}else {
			prepaidCxOfferEventCondition = PrepaidCxOfferEventCondition.builder()
					.offerConfigId(offerConfigId)
					.build();
		}
		prepaidCxOfferEventCondition.setEventConditionType(saveConfigRequest.getPayload().getOfferEventCondition().getEventConditionType());
		prepaidCxOfferEventCondition.setEventConditionName(saveConfigRequest.getPayload().getOfferEventCondition().getEventConditionName());
		if(saveConfigRequest.getPayload().getOfferEventCondition().getCampaignStartDate() != null) {
			prepaidCxOfferEventCondition.setCampaignStartDate(DateUtil.stringToDate(saveConfigRequest.getPayload().getOfferEventCondition().getCampaignStartDate()));
		}
		if(saveConfigRequest.getPayload().getOfferEventCondition().getCampaignEndDate() != null) {
			prepaidCxOfferEventCondition.setCampaignEndDate(DateUtil.stringToDate(saveConfigRequest.getPayload().getOfferEventCondition().getCampaignEndDate()));
		}

		if("Top-Up".equals(prepaidCxOfferEventCondition.getEventConditionType())){
			prepaidCxOfferEventCondition.setCreditMethod(saveConfigRequest.getPayload().getOfferEventCondition().getCreditMethod());
			prepaidCxOfferEventCondition.setUsageServiceType(saveConfigRequest.getPayload().getOfferEventCondition().getUsageServiceType());
			prepaidCxOfferEventCondition.setOperatorId(saveConfigRequest.getPayload().getOfferEventCondition().getOperatorId());
			prepaidCxOfferEventCondition.setTopUpCode(saveConfigRequest.getPayload().getOfferEventCondition().getTopUpCode());

			prepaidCxOfferEventCondition.setTopUpCurBalanceOp(saveConfigRequest.getPayload().getOfferEventCondition().getTopUpCurBalanceOp());
			prepaidCxOfferEventCondition.setTopUpCurBalanceValue(saveConfigRequest.getPayload().getOfferEventCondition().getTopUpCurBalanceValue());
			prepaidCxOfferEventCondition.setTopUpAccBalanceBeforeOp(saveConfigRequest.getPayload().getOfferEventCondition().getTopUpAccBalanceBeforeOp());
			prepaidCxOfferEventCondition.setTopUpAccBalanceBeforeValue(saveConfigRequest.getPayload().getOfferEventCondition().getTopUpAccBalanceBeforeValue());
			prepaidCxOfferEventCondition.setTopUpOp(saveConfigRequest.getPayload().getOfferEventCondition().getTopUpOp());
			prepaidCxOfferEventCondition.setTopUpTransactionValue(saveConfigRequest.getPayload().getOfferEventCondition().getTopUpTransactionValue());

			prepaidCxOfferEventCondition.setDaId(saveConfigRequest.getPayload().getOfferEventCondition().getDaId());
			prepaidCxOfferEventCondition.setDaBalanceOp(saveConfigRequest.getPayload().getOfferEventCondition().getDaBalanceOp());
			prepaidCxOfferEventCondition.setDaBalanceValue(saveConfigRequest.getPayload().getOfferEventCondition().getDaBalanceValue());
			prepaidCxOfferEventCondition.setTempServiceClass(saveConfigRequest.getPayload().getOfferEventCondition().getTempServiceClass());

		}else if("ARPU".equals(prepaidCxOfferEventCondition.getEventConditionType())){
			prepaidCxOfferEventCondition.setOperatorId(saveConfigRequest.getPayload().getOfferEventCondition().getOperatorId());
			prepaidCxOfferEventCondition.setArpuType(saveConfigRequest.getPayload().getOfferEventCondition().getArpuType());
			prepaidCxOfferEventCondition.setArpuOp(saveConfigRequest.getPayload().getOfferEventCondition().getArpuOp());
			prepaidCxOfferEventCondition.setArpuValue(saveConfigRequest.getPayload().getOfferEventCondition().getArpuValue());
			prepaidCxOfferEventCondition.setAggregationPeriodDays(saveConfigRequest.getPayload().getOfferEventCondition().getAggregationPeriodDays());
			prepaidCxOfferEventCondition.setArpuSelectedTopUpCode(saveConfigRequest.getPayload().getOfferEventCondition().getArpuSelectedTopUpCode());

		}else if("Usage".equals(prepaidCxOfferEventCondition.getEventConditionType())){
			prepaidCxOfferEventCondition.setUsageServiceType(saveConfigRequest.getPayload().getOfferEventCondition().getUsageServiceType());
			prepaidCxOfferEventCondition.setEventTypeUsages(saveConfigRequest.getPayload().getOfferEventCondition().getEventTypeUsages());
			prepaidCxOfferEventCondition.setEventUsagesOp(saveConfigRequest.getPayload().getOfferEventCondition().getEventUsagesOp());
			prepaidCxOfferEventCondition.setEventUsagesValue(saveConfigRequest.getPayload().getOfferEventCondition().getEventUsagesValue());
			prepaidCxOfferEventCondition.setCountryCode(saveConfigRequest.getPayload().getOfferEventCondition().getCountryCode());
			prepaidCxOfferEventCondition.setAggregationPeriodDays(saveConfigRequest.getPayload().getOfferEventCondition().getAggregationPeriodDays());

			prepaidCxOfferEventCondition.setDaId(saveConfigRequest.getPayload().getOfferEventCondition().getDaId());
			prepaidCxOfferEventCondition.setDaBalanceOp(saveConfigRequest.getPayload().getOfferEventCondition().getDaBalanceOp());
			prepaidCxOfferEventCondition.setDaBalanceValue(saveConfigRequest.getPayload().getOfferEventCondition().getDaBalanceValue());
			prepaidCxOfferEventCondition.setTempServiceClass(saveConfigRequest.getPayload().getOfferEventCondition().getTempServiceClass());
		}

		prepaidCxOfferEventConditionRepository.save(prepaidCxOfferEventCondition);
	}

}
