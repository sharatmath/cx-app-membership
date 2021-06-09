package com.dev.prepaid.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.prepaid.InitData;
import com.dev.prepaid.domain.PrepaidCxProvApplication;
import com.dev.prepaid.domain.PrepaidCxProvInstances;
import com.dev.prepaid.model.configuration.SaveConfigRequest;
import com.dev.prepaid.model.create.ServiceInstance;
import com.dev.prepaid.model.install.AppInstall;
import com.dev.prepaid.repository.PrepaidCxProvApplicationRepository;
import com.dev.prepaid.repository.PrepaidCxProvInstancesRepository;
import com.dev.prepaid.util.GUIDUtil;
import com.dev.prepaid.util.GsonUtils;

@Service
public class PrepaidCxServiceImpl implements PrepaidCxService {

	@Autowired
	private PrepaidCxProvApplicationRepository prepaidCxProvApplicationRepository;
	
	@Autowired
	private PrepaidCxProvInstancesRepository prepaidCxProvInstancesRepository;

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
		
		PrepaidCxProvApplication prepaidCxProvApplication = prepaidCxProvApplicationRepository
				.findOneByInstallIdAndAppIdAndUninstallDateIsNull
				(serviceInstance.getApplicationServiceInstall().getInstallUuid(), 
				 serviceInstance.getApplicationServiceInstall().getApplication().getUuid());
		
		if(prepaidCxProvApplication == null) return; //return or throw exception
		
		PrepaidCxProvInstances prepaidCxProvInstances = prepaidCxProvInstancesRepository
				.findOneByApplicationIdAndServiceIdAndInstanceIdAndDeletedDateIsNull
				(prepaidCxProvApplication.getId(), 
				 serviceInstance.getApplicationServiceInstall().getUuid(), 
				 serviceInstance.getUuid());
		
		if(prepaidCxProvInstances == null) {	
			prepaidCxProvInstances = new PrepaidCxProvInstances().builder()
					.id(GUIDUtil.generateGUID())
					
					.applicationId(prepaidCxProvApplication.getId())
					.serviceId(serviceInstance.getApplicationServiceInstall().getUuid())
					.instanceId(serviceInstance.getUuid())
					.programId(serviceInstance.getAssetId())
					
					.status("CREATED")
					.createdBy(serviceInstance.getApplicationServiceInstall().getName())
					.createdDate(new Date())
					.build();
			
			prepaidCxProvInstancesRepository.save(prepaidCxProvInstances);
		}else {
			
			prepaidCxProvInstances.setStatus("CREATED"); //CREATED
			prepaidCxProvInstances.setLastModifiedBy(serviceInstance.getApplicationServiceInstall().getName());
			prepaidCxProvInstances.setLastModifiedDate(new Date());
			
			prepaidCxProvInstancesRepository.save(prepaidCxProvInstances);
		}
	}
	
	@Override
	public void appConfigurationAddEntity(SaveConfigRequest saveConfigRequest) {
		Boolean notification = (saveConfigRequest.getPayload().getNotification() != null) ? saveConfigRequest.getPayload().getNotification() : false;
		// TODO update entity
		PrepaidCxProvInstances prepaidCxProvInstances = prepaidCxProvInstancesRepository
				.findOneByInstanceIdAndDeletedDateIsNull(saveConfigRequest.getInstanceUuid());
		
		if(prepaidCxProvInstances != null) {	
//			prepaidCxProvInstances.setStartDate(saveConfigRequest.getPayload().getStartDate());
//			prepaidCxProvInstances.setEndDate(saveConfigRequest.getPayload().getEndDate());
			prepaidCxProvInstances.setCampaignOfferType(saveConfigRequest.getPayload().getOfferType());
			prepaidCxProvInstances.setCampaignOfferId(saveConfigRequest.getPayload().getOfferCampaignId());
			prepaidCxProvInstances.setInputMapping(GsonUtils.deserializeObjectToJSON(InitData.recordDefinition.getInputParameters()));
			prepaidCxProvInstances.setOutputMapping(GsonUtils.deserializeObjectToJSON(InitData.recordDefinition.getOutputParameters()));			
			
			prepaidCxProvInstances.setNotification(notification);
			
			prepaidCxProvInstances.setStatus("CONFIGURED");
			prepaidCxProvInstances.setLastModifiedDate(new Date());
			
			prepaidCxProvInstancesRepository.save(prepaidCxProvInstances);
			
		}
		
	}

	@Override
	public void appDeleteEntity(String instanceUuid) {
		PrepaidCxProvInstances prepaidCxProvInstances = prepaidCxProvInstancesRepository
				.findOneByInstanceIdAndDeletedDateIsNull(instanceUuid);
		
		prepaidCxProvInstances.setDeletedBy("-");
		prepaidCxProvInstances.setDeletedDate(new Date());		
		
		prepaidCxProvInstancesRepository.save(prepaidCxProvInstances);
	}

	@Override
	public void appUninstallEntity(String installId, String appId) {
		PrepaidCxProvApplication prepaidCxProvApplication = prepaidCxProvApplicationRepository
				.findOneByInstallIdAndAppIdAndUninstallDateIsNull(installId, appId);
		
		prepaidCxProvApplication.setUninstallBy("-");
		prepaidCxProvApplication.setUninstallDate(new Date());
		
		prepaidCxProvApplicationRepository.save(prepaidCxProvApplication);		
	}

}
