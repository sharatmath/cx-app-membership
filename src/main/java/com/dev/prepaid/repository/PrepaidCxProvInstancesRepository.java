package com.dev.prepaid.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dev.prepaid.domain.PrepaidCxProvInstances;

@Repository
public interface PrepaidCxProvInstancesRepository extends CrudRepository<PrepaidCxProvInstances, String> {
	
	//CREATE
	PrepaidCxProvInstances findOneByServiceIdAndInstanceIdAndDeletedDateIsNull(String serviceId, String instanceId);
	PrepaidCxProvInstances findOneByApplicationIdAndServiceIdAndInstanceIdAndDeletedDateIsNull
	(String applicationId, String serviceId, String instanceId);
	
	//CONFIGURE //INVOKE //DELETE
	PrepaidCxProvInstances findOneByInstanceIdAndDeletedDateIsNull(String instanceId);
	
	

//	PrepaidCxProvInstances findOneByServiceIdAndStatusAndDeletedDateIsNull(String serviceId,String status);
//	PrepaidCxProvInstances findOneByServiceIdAndDeletedDateIsNull(String serviceId);
//	PrepaidCxProvInstances findOneByIdAndStatusAndDeletedDateIsNull(String instanceId,String status);
	
//	PrepaidCxProvInstances findOneByIdAndCampaignOfferIdAndDeletedDateIsNull(String id, Integer offerId);
//	PrepaidCxProvInstances findOneByIdAndDeletedDateIsNull(String id);
	
	
	
}
