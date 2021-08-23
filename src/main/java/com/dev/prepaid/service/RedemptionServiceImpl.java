package com.dev.prepaid.service;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RedemptionServiceImpl implements RedemptionService {	

	@Autowired
	EntityManager em;

	@Override
	public void processByCall(String msisdn) {
		System.out.println("Service : "+ msisdn);
	}
	 

//	@Autowired
//	private PrepaidOmsOfferBucketRepository prepaidOmsOfferBucketRepository;
//	@Autowired
//	private PrepaidOmsOfferCampaignRepository prepaidOmsOfferCampaignRepository;
//	@Autowired
//	private PrepaidDaOfferBucketRepository prepaidDaOfferBucketRepository;
//	@Autowired
//	private PrepaidDaOfferCampaignRepository prepaidDaOfferCampaignRepository;
//	@Autowired
//	private PrepaidCxOfferConfigRepository prepaidCxOfferConfigRepository;
//	@Autowired
//	private PrepaidCxOfferSelectionRepository prepaidCxOfferSelectionRepository;
//	@Autowired
//	private PrepaidCxOfferEligibilityRepository  prepaidCxOfferEligibilityRepository;
//	@Autowired
//	private PrepaidCxOfferMonitoringRepository prepaidCxOfferMonitoringRepository;
//	@Autowired
//	private PrepaidCxOfferRedemptionRepository prepaidCxOfferRedemptionRepository;
//	@Autowired
//	private PrepaidCxOfferEventConditionRepository prepaidCxOfferEventConditionRepository;
//	@Autowired
//	private CountryRepository countryRepository;

	

}