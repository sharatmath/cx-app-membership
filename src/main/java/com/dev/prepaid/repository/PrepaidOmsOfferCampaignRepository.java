package com.dev.prepaid.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dev.prepaid.domain.PrepaidOmsOfferCampaign;

@Repository
public interface PrepaidOmsOfferCampaignRepository extends CrudRepository<PrepaidOmsOfferCampaign, Long>{
	
//	List<PrepaidOmsOfferCampaign> findAllByOfferIdAndDeletedDateIsNull(Long id);
	List<PrepaidOmsOfferCampaign> findAllByOfferId(Long id);
	PrepaidOmsOfferCampaign findOneById(Long id);
	
}
