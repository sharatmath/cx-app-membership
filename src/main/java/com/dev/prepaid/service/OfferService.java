package com.dev.prepaid.service;

import java.util.List;

import com.dev.prepaid.domain.*;
import com.dev.prepaid.model.configuration.OfferEligibility;
import com.dev.prepaid.model.configuration.OfferMonitoring;
import com.dev.prepaid.model.configuration.OfferRedemption;
import com.dev.prepaid.model.configuration.OfferSelection;


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

	public List<PrepaidCxOfferSelection> getOfferSelection(String instanceId);
	public PrepaidCxOfferEligibility getOfferEligibility(String instanceId);
	public PrepaidCxOfferMonitoring getOfferMonitoring(String instanceId);
	public PrepaidCxOfferRedemption getOfferRedemption(String instanceId);
}
