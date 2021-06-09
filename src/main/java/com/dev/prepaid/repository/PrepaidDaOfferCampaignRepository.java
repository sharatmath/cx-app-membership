package com.dev.prepaid.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dev.prepaid.domain.PrepaidDaOfferCampaign;

@Repository
public interface PrepaidDaOfferCampaignRepository extends CrudRepository<PrepaidDaOfferCampaign, Long>{
	
//	List<PrepaidDaOfferCampaign> findAllByOfferIdAndDeletedDateIsNull(Long id);
	List<PrepaidDaOfferCampaign> findAllByOfferId(Long id);
	PrepaidDaOfferCampaign findOneById(Long id);
	
}
