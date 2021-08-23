package com.dev.prepaid.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.prepaid.domain.PrepaidCxOfferRedemption;
import com.dev.prepaid.domain.PrepaidOfferMembership;
import com.dev.prepaid.repository.PrepaidCxOfferRedemptionRepository;
import com.dev.prepaid.repository.PrepaidOfferMembershipRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RedemptionServiceImpl implements RedemptionService {	

	@Autowired
	EntityManager em;
	
	
	@Autowired
	private PrepaidCxOfferRedemptionRepository prepaidCxOfferRedemptionRepository;
	
	@Autowired
	private PrepaidOfferMembershipRepository prepaidOfferMembershipRepository;

	@Override
	public void processByCall(String msisdn) {
		log.info("msisdn : "+ msisdn);
		
		ArrayList<PrepaidCxOfferRedemption> prepaidCxOfferRedemptions = (ArrayList<PrepaidCxOfferRedemption>) prepaidCxOfferRedemptionRepository.findAll() ;
		
		PrepaidCxOfferRedemption pCxOfferRedemption = prepaidCxOfferRedemptions.get(0); //take first row
		log.info(pCxOfferRedemption.getOfferConfigId());
		
		Query q= null;
		String sql="";
		List<PrepaidOfferMembership> membership =null;
		
		//1.---Total Redemption Cap---
		//1.1 Total number of redemption radio button
		if(pCxOfferRedemption.getIsRedemptionCapOnly()) {
			sql="select * from prepaid_offer_membership "
					+ "where "
					+ "offer_config_id='"+pCxOfferRedemption.getOfferConfigId()+"' and "
					+ "redemption_date is null "
					+ "order by offer_membership_id FETCH NEXT "+pCxOfferRedemption.getRedemptionCapValue()+" ROWS ONLY";
			log.info("sql="+sql);
			
			q=em.createNativeQuery(sql);
			membership = q.getResultList();
		}
		//1.2 Redemption and Time Period
		else if (pCxOfferRedemption.getIsRedemptionCapAndPeriod()) {
			sql="select * from prepaid_offer_membership "
					+ "where "
					+ "offer_config_id='"+pCxOfferRedemption.getOfferConfigId()+"' and "
					+ "redemption_date is null "
					+ "order by offer_membership_id FETCH NEXT "+pCxOfferRedemption.getRedemptionCapValue()+" ROWS ONLY";
			log.info("sql="+sql);
			
			q=em.createNativeQuery(sql);
			membership = q.getResultList();
		}
		
//		prepaidOfferMembershipRepository.find
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