package com.dev.prepaid.service;

import java.util.List;

import com.dev.prepaid.domain.PrepaidDaOfferBucket;
import com.dev.prepaid.domain.PrepaidDaOfferCampaign;
import com.dev.prepaid.domain.PrepaidOmsOfferBucket;
import com.dev.prepaid.domain.PrepaidOmsOfferCampaign;


public interface OfferService {

	public List<PrepaidOmsOfferBucket> listOmsOfferBucket();
	public List<PrepaidOmsOfferCampaign> listOmsOfferCampaign(Long offerId);
	public PrepaidOmsOfferCampaign getOmsOfferCampaign(Long id);
	public PrepaidOmsOfferBucket getOmsOfferBucket(Long id);
	public PrepaidOmsOfferBucket getOmsOfferBucketByCode(String code);
	
	public List<PrepaidDaOfferBucket> listDaOfferBucket();
	public List<PrepaidDaOfferCampaign> listDaOfferCampaign(Long offerId);
	public PrepaidDaOfferCampaign getDaOfferCampaign(Long id);
	public PrepaidDaOfferBucket getDaOfferBucket(Long id);
	public PrepaidDaOfferBucket getDaOfferBucketByCode(String code);
	
	public void evictAllCaches();
}
