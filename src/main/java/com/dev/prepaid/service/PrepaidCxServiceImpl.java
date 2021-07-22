package com.dev.prepaid.service;

import java.util.Date;
import java.util.Optional;

import com.dev.prepaid.domain.*;
import com.dev.prepaid.model.configuration.OfferSelection;
import com.dev.prepaid.repository.*;
import lombok.extern.slf4j.Slf4j;
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
	
//	@Autowired
//	private PrepaidCxProvInstancesRepository prepaidCxProvInstancesRepository;

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
		
//		PrepaidCxProvInstances prepaidCxProvInstances = prepaidCxProvInstancesRepository
//				.findOneByApplicationIdAndServiceIdAndInstanceIdAndDeletedDateIsNull
//				(prepaidCxProvApplication.getId(),
//				 serviceInstance.getApplicationServiceInstall().getUuid(),
//				 serviceInstance.getUuid());

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

//		if(prepaidCxProvInstances == null) {
//			prepaidCxProvInstances = new PrepaidCxProvInstances().builder()
//					.id(GUIDUtil.generateGUID())
//
//					.applicationId(prepaidCxProvApplication.getId())
//					.serviceId(serviceInstance.getApplicationServiceInstall().getUuid())
//					.instanceId(serviceInstance.getUuid())
//					.programId(serviceInstance.getAssetId())
//
//					.status("CREATED")
//					.createdBy(serviceInstance.getApplicationServiceInstall().getName())
//					.createdDate(new Date())
//					.build();
//
//			prepaidCxProvInstancesRepository.save(prepaidCxProvInstances);
//		}else {
//
//			prepaidCxProvInstances.setStatus("CREATED"); //CREATED
//			prepaidCxProvInstances.setLastModifiedBy(serviceInstance.getApplicationServiceInstall().getName());
//			prepaidCxProvInstances.setLastModifiedDate(new Date());
//
//			prepaidCxProvInstancesRepository.save(prepaidCxProvInstances);
//		}
	}
	
	@Override
	public void appConfigurationAddEntity(SaveConfigRequest saveConfigRequest) {
		Boolean notification = (saveConfigRequest.getPayload().getNotification() != null) ? saveConfigRequest.getPayload().getNotification() : false;
		// TODO update entity
//		PrepaidCxProvInstances prepaidCxProvInstances = prepaidCxProvInstancesRepository
//				.findOneByInstanceIdAndDeletedDateIsNull(saveConfigRequest.getInstanceUuid());

		Optional<PrepaidCxOfferConfig> opsFind = prepaidCxOfferConfigRepository.findByInstanceId(saveConfigRequest.getInstanceUuid());
		PrepaidCxOfferConfig prepaidCxOfferConfig = null;
		log.debug("get data  {} ", opsFind);
		if(opsFind.isPresent()) {
			opsFind.get().setProgramId(saveConfigRequest.getPayload().getProgramId());
			opsFind.get().setProgramName(saveConfigRequest.getPayload().getProgramName());
			opsFind.get().setProvisionType(saveConfigRequest.getPayload().getProvisionType());

			prepaidCxOfferConfig = prepaidCxOfferConfigRepository.save(opsFind.get());

			if(prepaidCxOfferConfig.getId() != null){
				saveOfferSelection(prepaidCxOfferConfig.getId(), saveConfigRequest);
				saveOfferEligibility(prepaidCxOfferConfig.getId(), saveConfigRequest);
				saveOfferMonitoring(prepaidCxOfferConfig.getId(), saveConfigRequest);
				saveOfferRedemption(prepaidCxOfferConfig.getId(), saveConfigRequest);
			}

//			for(OfferSelection offerSelection : saveConfigRequest.getPayload().getOfferSelections()) {
	//			prepaidCxProvInstances.setStartDate(saveConfigRequest.getPayload().getStartDate());
	//			prepaidCxProvInstances.setEndDate(saveConfigRequest.getPayload().getEndDate());
//				prepaidCxProvInstances.setCampaignOfferType(offerSelection.getOfferBucketType());
//				prepaidCxProvInstances.setCampaignOfferId(offerSelection.getOfferCampaignId());
//				prepaidCxProvInstances.setInputMapping(GsonUtils.deserializeObjectToJSON(InitData.recordDefinition.getInputParameters()));
//				prepaidCxProvInstances.setOutputMapping(GsonUtils.deserializeObjectToJSON(InitData.recordDefinition.getOutputParameters()));
//
//				prepaidCxProvInstances.setNotification(notification);
//
//				prepaidCxProvInstances.setStatus("CONFIGURED");
//				prepaidCxProvInstances.setLastModifiedDate(new Date());
//
//				prepaidCxProvInstancesRepository.save(prepaidCxProvInstances);
//				log.trace("save {} ", prepaidCxProvInstances);
//			}
			
		}
		
	}

	@Override
	public void appDeleteEntity(String instanceUuid) {
//		PrepaidCxProvInstances prepaidCxProvInstances = prepaidCxProvInstancesRepository
//				.findOneByInstanceIdAndDeletedDateIsNull(instanceUuid);

		PrepaidCxOfferConfig prepaidCxOfferConfig = prepaidCxOfferConfigRepository.
				findOneByInstanceIdAndDeletedDateIsNull(instanceUuid);

		if(prepaidCxOfferConfig != null) {
			prepaidCxOfferConfig.setDeletedBy("-");
			prepaidCxOfferConfig.setDeletedDate(new Date());

			prepaidCxOfferConfigRepository.save(prepaidCxOfferConfig);
		}
//		prepaidCxProvInstancesRepository.save(prepaidCxProvInstances);
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
		PrepaidCxOfferEligibility prepaidCxOfferEligibility = PrepaidCxOfferEligibility.builder()
				.offerConfigId(offerConfigId)
				.isFrequencyOnly(false)
				.isFrequencyAndTime(false)
				.isOfferLevelCapOnly(false)
				.isOfferLevelCapAndPeriod(false)
				.build();

		if(saveConfigRequest.getPayload().getOfferEligibility().getIsFrequencyOnly()){
			prepaidCxOfferEligibility.setIsFrequencyOnly(true);
			prepaidCxOfferEligibility.setFrequency(Long.valueOf(1));
		}
		if(saveConfigRequest.getPayload().getOfferEligibility().getIsFrequencyAndTime()){
			prepaidCxOfferEligibility.setIsOfferLevelCapAndPeriod(true);
			prepaidCxOfferEligibility.setNumberOfFrequency(saveConfigRequest.getPayload().getOfferEligibility().getNumberOfFrequency());
			prepaidCxOfferEligibility.setNumberOfDays(saveConfigRequest.getPayload().getOfferEligibility().getNumberOfDays());
		}
		if(saveConfigRequest.getPayload().getOfferEligibility().getIsOfferLevelCapOnly()){
			prepaidCxOfferEligibility.setIsOfferLevelCapOnly(true);
			prepaidCxOfferEligibility.setOfferLevelCapValue(Long.valueOf(saveConfigRequest.getPayload().getOfferEligibility().getOfferLevelCapValue()));
		}else if(saveConfigRequest.getPayload().getOfferEligibility().getIsOfferLevelCapAndPeriod()){
			prepaidCxOfferEligibility.setIsOfferLevelCapAndPeriod(true);
			prepaidCxOfferEligibility.setOfferLevelCapPeriodValue(saveConfigRequest.getPayload().getOfferEligibility().getOfferLevelCapPeriodValue());
			prepaidCxOfferEligibility.setOfferLevelCapPeriodDays(saveConfigRequest.getPayload().getOfferEligibility().getOfferLevelCapPeriodDays());

		}
		prepaidCxOfferEligibilityRepository.save(prepaidCxOfferEligibility);
	}

	private void saveOfferSelection(String offerConfigId, SaveConfigRequest saveConfigRequest){
		for(OfferSelection offerSelection: saveConfigRequest.getPayload().getOfferSelections()){
			PrepaidCxOfferSelection prepaidCxOfferSelection = PrepaidCxOfferSelection.builder()
					.offerConfigId(offerConfigId)
					.offerBucketType(offerSelection.getOfferBucketType())
					.offerBucketId(offerSelection.getOfferBucketId())
					.offerType(offerSelection.getOfferCampaignName())
					.offerId(String.valueOf(offerSelection.getOfferCampaignId()))
					.build();

			prepaidCxOfferSelectionRepository.save(prepaidCxOfferSelection);
		}
	}

	private void saveOfferMonitoring(String offerConfigId, SaveConfigRequest saveConfigRequest){
		PrepaidCxOfferMonitoring prepaidCxOfferMonitoring = PrepaidCxOfferMonitoring.builder()
				.offerConfigId(offerConfigId)
				.eventType(saveConfigRequest.getPayload().getOfferMonitoring().getEventType())
				.usageServiceType(saveConfigRequest.getPayload().getOfferMonitoring().getUsageServiceType())
				.productPackage(saveConfigRequest.getPayload().getOfferMonitoring().getProductPackage())
				.creditMethod(saveConfigRequest.getPayload().getOfferMonitoring().getCreditMethod())
				.operatorId(saveConfigRequest.getPayload().getOfferMonitoring().getOperatorId())
				.periodDays(saveConfigRequest.getPayload().getOfferMonitoring().getPeriodDays())
				.periodEndDate(saveConfigRequest.getPayload().getOfferMonitoring().getPeriodEndDate())
				.periodStartDate(saveConfigRequest.getPayload().getOfferMonitoring().getPeriodStartDate())
				.topupCode(saveConfigRequest.getPayload().getOfferMonitoring().getTopupCode())
				.usageType(saveConfigRequest.getPayload().getOfferMonitoring().getUsageType())
				.transactionValue(saveConfigRequest.getPayload().getOfferMonitoring().getTransactionValue())
				.operatorValue(saveConfigRequest.getPayload().getOfferMonitoring().getOperatorValue())
				.paidArpuOperator(saveConfigRequest.getPayload().getOfferMonitoring().getPaidArpuOperator())
				.isMonitorDateRange(saveConfigRequest.getPayload().getOfferMonitoring().getIsMonitorDateRange())
				.isMonitorSpecificPeriod(saveConfigRequest.getPayload().getOfferMonitoring().getIsMonitorSpecificPeriod())
				.build();

		prepaidCxOfferMonitoringRepository.save(prepaidCxOfferMonitoring);
	}

	private void saveOfferRedemption(String offerConfigId, SaveConfigRequest saveConfigRequest){
		PrepaidCxOfferRedemption prepaidCxOfferRedemption = PrepaidCxOfferRedemption.builder()
				.offerConfigId(offerConfigId)
				.isRedemptionCapOnly(saveConfigRequest.getPayload().getOfferRedemption().getIsRedemptionCapOnly())
				.redemptionCapValue(saveConfigRequest.getPayload().getOfferRedemption().getRedemptionCapValue())
				.isRedemptionCapAndPeriod(saveConfigRequest.getPayload().getOfferRedemption().getIsRedemptionCapAndPeriod())
				.totalRedemptionPeriodValue(saveConfigRequest.getPayload().getOfferRedemption().getTotalRedemptionPeriodValue())
				.isFrequencyOnly(saveConfigRequest.getPayload().getOfferRedemption().getIsFrequencyOnly())
				.isFrequencyAndTime(saveConfigRequest.getPayload().getOfferRedemption().getIsFrequencyAndTime())
				.frequencyValue(saveConfigRequest.getPayload().getOfferRedemption().getFrequencyValue())
				.timePeriodValue(saveConfigRequest.getPayload().getOfferRedemption().getTimePeriodValue())
				.timePeriodType(saveConfigRequest.getPayload().getOfferRedemption().getTimePeriodType())
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
				.build();

		prepaidCxOfferRedemptionRepository.save(prepaidCxOfferRedemption);
	}

}
