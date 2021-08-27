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
		List<PrepaidOfferMembership> memberships =null;
		
		int iTotal = 0;
		Boolean bProcess=false;
		
		//---Recurring---
		if (pCxOfferRedemption.getTotalRecurringFrequency()!=null &&
			pCxOfferRedemption.getTotalRecurringFrequency() > 0 ) {
			
			sql="select count(*) as counts from prepaid_offer_membership "
				+ "where "
				+ "offer_config_id='"+pCxOfferRedemption.getOfferConfigId()+"' and "
				+ "msisdn ="+msisdn+" and "
				+ "redemption_date is not null ";
			log.info("sql="+sql);
				
			q=em.createNativeQuery(sql);
			iTotal = ((Number)q.getSingleResult()).intValue();
			
			if (iTotal >= pCxOfferRedemption.getTotalRecurringFrequency())
				return;
		
			//Recurring radion batton 1
			if (pCxOfferRedemption.getIsRecurringFrequencyAndPeriod()) {
				if (pCxOfferRedemption.getRecurringFrequencyPeriodType().equalsIgnoreCase("days")) {
					sql="select count(*) as counts from prepaid_offer_membership "
						+ "where "
						+ "offer_config_id='"+pCxOfferRedemption.getOfferConfigId()+"' and "
						+ "MSISDN ="+msisdn+" and "
						+ "(redemption_date is not null and redemption_date = trunc(sysdate - "+pCxOfferRedemption.getRecurringFrequencyPeriodValue()+")) ";
				}
				else if (pCxOfferRedemption.getRecurringFrequencyPeriodType().equalsIgnoreCase("months")) {
					sql="select count(*) as counts from prepaid_offer_membership "
						+ "where "
						+ "offer_config_id='"+pCxOfferRedemption.getOfferConfigId()+"' and "
						+ "MSISDN ="+msisdn+" and "
						+ "(redemption_date is not null and redemption_date = redemption_date = trunc(add_months(sysdate,-"+pCxOfferRedemption.getRecurringFrequencyPeriodValue()+"))) ";
			}
				log.info("sql="+sql);
				
				q=em.createNativeQuery(sql);
				iTotal = ((Number)q.getSingleResult()).intValue() ;
	
				if (iTotal < pCxOfferRedemption.getRecurringFrequencyValue()) 
					bProcess=true;
				else
					bProcess=false;
			
			}
		
			//Recurring radion batton 2
			else if (pCxOfferRedemption.getIsRecurringFrequencyEachMonth()){
				sql="select count(*) as counts from prepaid_offer_membership "
					+ "where "
					+ "offer_config_id='"+pCxOfferRedemption.getOfferConfigId()+"' and "
					+ "msisdn ="+msisdn+" and "
					+ "where to_char(redemption_date,'MON-YYYY') = to_char(sysdate, 'MON-YYYY') ";
				log.info("sql="+sql);
				
				q=em.createNativeQuery(sql);
				iTotal = ((Number)q.getSingleResult()).intValue() ;
	
				if (iTotal < pCxOfferRedemption.getRecurringFrequencyDayOfMonth())
					bProcess=true;
				else
					bProcess=false;
			}
		}//---End of Recurring
		
		
		if (bProcess==true) {
			//1.---Total Redemption Cap---
			//1.1 Total number of redemption radio button
			if(pCxOfferRedemption.getIsRedemptionCapOnly()) {
				sql="select count(*) as counts  from prepaid_offer_membership "
					+ "where "
					+ "offer_config_id='"+pCxOfferRedemption.getOfferConfigId()+"' and "
					+ "msisdn ="+msisdn+" and "
					+ "redemption_date is null ";
				log.info("sql="+sql);
				
				q=em.createNativeQuery(sql);
				iTotal = ((Number)q.getSingleResult()).intValue();
				
				if (iTotal < pCxOfferRedemption.getRedemptionCapValue())
					bProcess=true;
				else
					bProcess=false;
				
			}// --- 1.1 Total number of redemption radio button
			
			//1.2 Redemption and Time Period
			else if (pCxOfferRedemption.getIsRedemptionCapAndPeriod()) {
				if (pCxOfferRedemption.getTotalRedemptionPeriodType().equalsIgnoreCase("days")) {
					sql="select count(*) as counts  from prepaid_offer_membership "
						+ "where "
						+ "offer_config_id='"+pCxOfferRedemption.getOfferConfigId()+"' and "
						+ "msisdn ="+msisdn+" and "
						+ "(redemption_date is not null and redemption_date = trunc(sysdate - "+pCxOfferRedemption.getTotalRedemptionPeriodEvery()+"))";
				}
				else if (pCxOfferRedemption.getTotalRedemptionPeriodType().equalsIgnoreCase("months")) {
					sql="select * from prepaid_offer_membership "
						+ "where "
						+ "offer_config_id='"+pCxOfferRedemption.getOfferConfigId()+"' and "
						+ "msisdn ="+msisdn+" and "
						+ "(redemption_date is not null and redemption_date = redemption_date = trunc(add_months(sysdate,-"+pCxOfferRedemption.getTotalRedemptionPeriodEvery()+")))";
				}
				log.info("sql="+sql);
				
				q=em.createNativeQuery(sql);
				iTotal = ((Number)q.getSingleResult()).intValue() ;

				if (iTotal < pCxOfferRedemption.getRedemptionCapValue() ) 
					bProcess=true;
				else
					bProcess=false;
			} // --- End of 1.2 Redemption and Time Period
			
			
			//2.---Subsricber Redemption Cap---
			if(pCxOfferRedemption.getIsFrequencyOnly()) {
				sql="select * from prepaid_offer_membership "
					+ "where "
					+ "offer_config_id='"+pCxOfferRedemption.getOfferConfigId()+"' and "
					+ "msisdn ="+msisdn+" and "
					+ "redemption_date is null ";
				log.info("sql="+sql);
				
				q=em.createNativeQuery(sql);
				iTotal = ((Number)q.getSingleResult()).intValue() ;

				if (iTotal < pCxOfferRedemption.getFrequencyValue() ) 
					bProcess=true;
				else
					bProcess=false;
			}
			else if (pCxOfferRedemption.getIsFrequencyAndTime()) {
				if (pCxOfferRedemption.getTimePeriodType().equalsIgnoreCase("days")) {
					sql="select * from prepaid_offer_membership "
						+ "where "
						+ "offer_config_id='"+pCxOfferRedemption.getOfferConfigId()+"' and "
						+ "msisdn ="+msisdn+" and "
						+ "(redemption_date is not null and redemption_date = trunc(sysdate - "+pCxOfferRedemption.getTotalRedemptionPeriodEvery()+"))";
				}
				else if (pCxOfferRedemption.getTimePeriodType().equalsIgnoreCase("months")) {
					sql="select * from prepaid_offer_membership "
						+ "where "
						+ "offer_config_id='"+pCxOfferRedemption.getOfferConfigId()+"' and "
						+ "msisdn ="+msisdn+" and "
						+ "(redemption_date is not null and redemption_date = redemption_date = trunc(add_months(sysdate,-"+pCxOfferRedemption.getTotalRedemptionPeriodEvery()+"))) ";
				}
				log.info("sql="+sql);
				
				q=em.createNativeQuery(sql);
				iTotal = ((Number)q.getSingleResult()).intValue() ;

				if (iTotal < pCxOfferRedemption.getRedemptionCapValue() ) 
					bProcess=true;
				else
					bProcess=false;
			}
			
		} // --- End of bProcess
		
		
		
		if (bProcess==true) {
			if(pCxOfferRedemption.getRedemptionMethod().equalsIgnoreCase("manual")) {
				log.info("Process with Manual, for msisdn:"+msisdn+" call API Responsys ");
			}
			else if(pCxOfferRedemption.getRedemptionMethod().equalsIgnoreCase("auto")) {
				log.info("Process with Auto, for msisdn:"+msisdn+" call API Provisoning ");
			}
		}

		
	}
	 

 

	

}