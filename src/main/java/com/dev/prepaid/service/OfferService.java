package com.dev.prepaid.service;

import java.util.List;

import com.dev.prepaid.domain.*;
import com.dev.prepaid.model.configuration.*;
import com.dev.prepaid.repository.PrepaidMaOfferBucketRepository;


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

	//MA Services
	public List<PrepaidMaCreditOffer> listMaOfferBucket();
	public PrepaidMaCreditOffer getMaCreditOfferById(Long id);
	public void deletePrepaidMaCreditOffer(Long id);
	public List<PrepaidMaCreditOffer> listMaByOffer(String programName);
//	public PrepaidMaCreditOffer getMaCreditOfferByProgramName(String programName);

	public void evictAllCaches();
	public OfferPromoCode getOfferPromoCode(String instanceId);
	public OfferNoneType getOfferNoneType(String instanceId);
	public List<OfferSelection> getOfferSelection(String instanceId);
	public PrepaidCxOfferEligibility getOfferEligibility(String instanceId);
	public OfferFulfilment getOfferMonitoring(String instanceId);
	public PrepaidCxOfferRedemption getOfferRedemption(String instanceId);
	public PrepaidCxOfferEventCondition getOfferEventCondition(String instanceId);
	public List<ResponSysProgram> listProgram();
	public List<Country> listCountry();
	public List<PromoCode> listOfferType();
	public OverallOfferName checkOverallOfferName(String overallOfferName);
	public String getProvisionType(String instanceId);
}
