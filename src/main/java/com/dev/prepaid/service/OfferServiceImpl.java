package com.dev.prepaid.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dev.prepaid.domain.*;
import com.dev.prepaid.model.configuration.*;
import com.dev.prepaid.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OfferServiceImpl implements OfferService {	

	@Autowired
	CacheManager cacheManager;
	
	@Value("${list.oms.master.offer}")
	private String listOmsMasterOffer;
	
	@Value("${list.oms.campaign.offer}")
	private String listOmsCampaignOffer;
	@Value("${get.oms.campaign.offer}")
	private String getOmsCampaignOffer;
	
	@Value("${list.da.master.offer}")
	private String listDaMasterOffer;

	@Value("${list.da.campaign.offer}")
	private String listDaCampaignOffer;
	@Value("${get.da.campaign.offer}")
	private String getDaCampaignOffer;

	@Autowired
	private PrepaidOmsOfferBucketRepository prepaidOmsOfferBucketRepository;
	@Autowired
	private PrepaidOmsOfferCampaignRepository prepaidOmsOfferCampaignRepository;
	@Autowired
	private PrepaidDaOfferBucketRepository prepaidDaOfferBucketRepository;
	@Autowired
	private PrepaidDaOfferCampaignRepository prepaidDaOfferCampaignRepository;
	@Autowired
	private PrepaidCxOfferConfigRepository prepaidCxOfferConfigRepository;
	@Autowired
	private PrepaidCxOfferSelectionRepository prepaidCxOfferSelectionRepository;
	@Autowired
	private PrepaidCxOfferEligibilityRepository  prepaidCxOfferEligibilityRepository;
	@Autowired
	private PrepaidCxOfferMonitoringRepository prepaidCxOfferMonitoringRepository;
	@Autowired
	private PrepaidCxOfferRedemptionRepository prepaidCxOfferRedemptionRepository;

	@Override
	public void evictAllCaches() {
		//clear caches
		cacheManager.getCacheNames().stream()
		.forEach(cacheName -> {
//			log.debug("{} evicted",cacheName);
			cacheManager.getCache(cacheName).clear();
		});
	}


	/// OMS
	@Override
//	@Cacheable(cacheNames = "listOmsOfferBucket", 
//			unless = "#result == null")
	public synchronized List<PrepaidOmsOfferBucket> listOmsOfferBucket() {
		return prepaidOmsOfferBucketRepository.findAll();
	}

	@Override
//	@Cacheable(cacheNames = "listOmsOfferCampaign",   
//			key = "#offerId",
//			unless = "#result == null")
	public synchronized List<PrepaidOmsOfferCampaign> listOmsOfferCampaign(Long offerId) {
		return prepaidOmsOfferCampaignRepository.findAllByOfferId(offerId);
	}

	@Override
	@Cacheable(cacheNames = "getOmsOfferBucket", 
			key = "#id",
			unless = "#result == null")
	public synchronized PrepaidOmsOfferBucket getOmsOfferBucket(Long id) {
		return prepaidOmsOfferBucketRepository.findOneById(id);
	}

	@Override
	@Cacheable(cacheNames = "getOmsOfferCampaign", 
			key = "#id",
			unless = "#result == null")
	public synchronized PrepaidOmsOfferCampaign getOmsOfferCampaign(Long id) {
		return prepaidOmsOfferCampaignRepository.findOneById(id);
	}

	@Override
//	@Cacheable(cacheNames = "getOmsOfferBucketByCode", 
//			key = "#id",
//			unless = "#result == null")
	public synchronized PrepaidOmsOfferBucket getOmsOfferBucketByCode(String code) {
		
		return prepaidOmsOfferBucketRepository.findOneByCode(code);
	}
	
	
	/// DA
	@Override
//	@Cacheable(cacheNames = "listDaOfferBucket", 
//			unless = "#result == null")
	public synchronized List<PrepaidDaOfferBucket> listDaOfferBucket() {
		return prepaidDaOfferBucketRepository.findAll();
	}

	@Override
//	@Cacheable(cacheNames = "listDaOfferCampaign",  
//			key = "#offerId",
//			unless = "#result == null")
	public synchronized List<PrepaidDaOfferCampaign> listDaOfferCampaign(Long offerId) {
		return prepaidDaOfferCampaignRepository.findAllByOfferId(offerId);
	}

	@Override
	@Cacheable(cacheNames = "getDaOfferBucket", 
			key = "#id",
			unless = "#result == null")
	public synchronized PrepaidDaOfferBucket getDaOfferBucket(Long id) {
		return prepaidDaOfferBucketRepository.findOneById(id);
	}

	@Override
	@Cacheable(cacheNames = "getDaOfferCampaign", 
			key = "#id",
			unless = "#result == null")
	public synchronized PrepaidDaOfferCampaign getDaOfferCampaign(Long id) {
		return prepaidDaOfferCampaignRepository.findOneById(id);
	}


	@Override
//	@Cacheable(cacheNames = "getDaOfferBucketByCode", 
//			key = "#id",
//			unless = "#result == null")
	public synchronized PrepaidDaOfferBucket getDaOfferBucketByCode(String code) {		
		return prepaidDaOfferBucketRepository.findOneByCode(code);
	}

	public List<PrepaidCxOfferSelection> getOfferSelection(String instanceId){
		Optional<PrepaidCxOfferConfig> offerConfig = prepaidCxOfferConfigRepository.findByInstanceId(instanceId);
		if(offerConfig.isPresent()){
			return prepaidCxOfferSelectionRepository.findByOfferConfigId(offerConfig.get().getId());
		}
		return null;
	}
	public PrepaidCxOfferEligibility getOfferEligibility(String instanceId){
		Optional<PrepaidCxOfferConfig> offerConfig = prepaidCxOfferConfigRepository.findByInstanceId(instanceId);
		if(offerConfig.isPresent()){
			Optional<PrepaidCxOfferEligibility> opsFind = prepaidCxOfferEligibilityRepository.findByOfferConfigId(offerConfig.get().getId());
			if(opsFind.isPresent())
				return opsFind.get();
		}
		return new PrepaidCxOfferEligibility();
	}
	public OfferFulfilment getOfferMonitoring(String instanceId){
		Optional<PrepaidCxOfferConfig> offerConfig = prepaidCxOfferConfigRepository.findByInstanceId(instanceId);
		OfferFulfilment offerFulfilment = new OfferFulfilment();
		if(offerConfig.isPresent()) {
			Optional<PrepaidCxOfferMonitoring> opsFind = prepaidCxOfferMonitoringRepository.findByOfferConfigId(offerConfig.get().getId());
			if (opsFind.isPresent()) {
				PrepaidCxOfferMonitoring prepaidCxOfferMonitoring = opsFind.get();
				offerFulfilment.setEventType(prepaidCxOfferMonitoring.getEventType());
				//monitoring
				if(prepaidCxOfferMonitoring.getPeriodEndDate() != null &&
					prepaidCxOfferMonitoring.getPeriodStartDate() != null ) {
					offerFulfilment.setMonitorSpecifiedPeriodRadio(true);
					offerFulfilment.setMonitorStartDate(prepaidCxOfferMonitoring.getPeriodStartDate());
					offerFulfilment.setMonitorEndDate(prepaidCxOfferMonitoring.getPeriodEndDate());
				}
				if(prepaidCxOfferMonitoring.getPeriod() != null &&
					prepaidCxOfferMonitoring.getPeriodDays() != null) {
					offerFulfilment.setMonitorPeriodRadio(true);
					offerFulfilment.setMonitorPeriod(prepaidCxOfferMonitoring.getPeriodDays());
					offerFulfilment.setMonitorPeriodDayMonth(prepaidCxOfferMonitoring.getPeriod());
				}

				if ("Top-Up".equals(prepaidCxOfferMonitoring.getEventType())) {
					offerFulfilment.setTopUpCreditMethod(prepaidCxOfferMonitoring.getCreditMethod());
					offerFulfilment.setTopUpProductPackage(prepaidCxOfferMonitoring.getProductPackage());
					offerFulfilment.setTopUpUsageServiceType(prepaidCxOfferMonitoring.getUsageServiceType());
					offerFulfilment.setTopUpOperator(prepaidCxOfferMonitoring.getOperatorId());
					offerFulfilment.setTopUpValueOperator(prepaidCxOfferMonitoring.getOperatorValue());
					offerFulfilment.setTopUpTransactionValue(String.valueOf(prepaidCxOfferMonitoring.getTransactionValue()));
					offerFulfilment.setTopUpCode(prepaidCxOfferMonitoring.getTopupCode());
					return offerFulfilment;
				} else if("ARPU".equals(prepaidCxOfferMonitoring.getEventType())){
					offerFulfilment.setArpuCreditMethod(prepaidCxOfferMonitoring.getCreditMethod());
					offerFulfilment.setArpuProductPackage(prepaidCxOfferMonitoring.getProductPackage());
					offerFulfilment.setArpuUsageServiceType(prepaidCxOfferMonitoring.getUsageServiceType());
					offerFulfilment.setArpuOperators(prepaidCxOfferMonitoring.getOperatorId());
					offerFulfilment.setArpuOperatorsValue (prepaidCxOfferMonitoring.getOperatorValue());
					offerFulfilment.setArpu(String.valueOf(prepaidCxOfferMonitoring.getTransactionValue()));
					offerFulfilment.setArpuPaidOperators(prepaidCxOfferMonitoring.getPaidArpuOperator());
					offerFulfilment.setArpuTopUpCode(String.valueOf(prepaidCxOfferMonitoring.getPaidArpuValue()));
					return offerFulfilment;
				}else if("Usage".equals(prepaidCxOfferMonitoring.getEventType())){
					offerFulfilment.setUsageServiceType(prepaidCxOfferMonitoring.getUsageServiceType());
					offerFulfilment.setUsageType(prepaidCxOfferMonitoring.getUsageType());
					offerFulfilment.setUsageOperator(prepaidCxOfferMonitoring.getOperatorValue());
					offerFulfilment.setUsageValue(String.valueOf(prepaidCxOfferMonitoring.getTransactionValue()));
					return  offerFulfilment;
				}
			}
		}
		return offerFulfilment;
	}
	public PrepaidCxOfferRedemption getOfferRedemption(String instanceId){
		Optional<PrepaidCxOfferConfig> offerConfig = prepaidCxOfferConfigRepository.findByInstanceId(instanceId);
		if(offerConfig.isPresent()){
			Optional<PrepaidCxOfferRedemption> opsFind =  prepaidCxOfferRedemptionRepository.findByOfferConfigId(offerConfig.get().getId());
			if(opsFind.isPresent())
				return opsFind.get();
		}
		return new PrepaidCxOfferRedemption();
	}

}