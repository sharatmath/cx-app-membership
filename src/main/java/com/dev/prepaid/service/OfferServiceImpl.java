package com.dev.prepaid.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dev.prepaid.domain.*;
import com.dev.prepaid.model.configuration.*;
import com.dev.prepaid.repository.*;
import com.dev.prepaid.util.DateUtil;
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
	@Autowired
	private PrepaidCxOfferEventConditionRepository prepaidCxOfferEventConditionRepository;
	@Autowired
	private CountryRepository countryRepository;
	@Autowired
	private PromoCodeRepository promoCodeRepository;

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
			List<PrepaidCxOfferSelection> prepaidCxOfferSelection= prepaidCxOfferSelectionRepository.findByOfferConfigId(offerConfig.get().getId());
			for(PrepaidCxOfferSelection p : prepaidCxOfferSelection){
				p.setOverallOfferName(offerConfig.get().getOverallOfferName());
			}
			return  prepaidCxOfferSelection;
		}
		return null;
	}
	public PrepaidCxOfferEventCondition getOfferEventCondition(String instanceId){
		Optional<PrepaidCxOfferConfig> offerConfig = prepaidCxOfferConfigRepository.findByInstanceId(instanceId);
		if(offerConfig.isPresent()){
			Optional<PrepaidCxOfferEventCondition> opsFind = prepaidCxOfferEventConditionRepository.findByOfferConfigId(offerConfig.get().getId());
			if(opsFind.isPresent())
				return opsFind.get();
		}
		return new PrepaidCxOfferEventCondition();
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
					try {
						offerFulfilment.setMonitorStartDate(DateUtil.fromDate(prepaidCxOfferMonitoring.getPeriodStartDate()));
						offerFulfilment.setMonitorEndDate(DateUtil.fromDate(prepaidCxOfferMonitoring.getPeriodEndDate()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				if(prepaidCxOfferMonitoring.getPeriod() != null &&
					prepaidCxOfferMonitoring.getPeriodDays() != null) {
					offerFulfilment.setMonitorPeriodRadio(true);
					offerFulfilment.setMonitorPeriod(prepaidCxOfferMonitoring.getPeriodDays());
					offerFulfilment.setMonitorPeriodDayMonth(prepaidCxOfferMonitoring.getPeriod());
				}

				if ("Top-Up".equals(prepaidCxOfferMonitoring.getEventType())) {
					offerFulfilment.setTopUpCreditMethod(prepaidCxOfferMonitoring.getCreditMethod());
					offerFulfilment.setTopUpUsageServiceType(prepaidCxOfferMonitoring.getUsageServiceType());
					offerFulfilment.setTopUpOperator(prepaidCxOfferMonitoring.getOperatorId());
					offerFulfilment.setTopUpCode(prepaidCxOfferMonitoring.getTopUpCode());

					offerFulfilment.setTopUpCurBalanceOp(prepaidCxOfferMonitoring.getTopUpCurBalanceOp());
					offerFulfilment.setTopUpCurBalanceValue(prepaidCxOfferMonitoring.getTopUpCurBalanceValue());

					offerFulfilment.setTopUpAccBalanceBeforeOp(prepaidCxOfferMonitoring.getTopUpAccBalanceBeforeOp());
					offerFulfilment.setTopUpAccBalanceBeforeValue(prepaidCxOfferMonitoring.getTopUpAccBalanceBeforeValue());

					offerFulfilment.setTopUpOp(prepaidCxOfferMonitoring.getTopUpOp());
					offerFulfilment.setTopUpTransactionValue(prepaidCxOfferMonitoring.getTopUpTransactionValue());

					offerFulfilment.setTopUpDaId(prepaidCxOfferMonitoring.getTopUpDaId());

					offerFulfilment.setTopUpDaBalanceOp(prepaidCxOfferMonitoring.getTopUpDaBalanceOp());
					offerFulfilment.setTopUpDaBalanceValue(prepaidCxOfferMonitoring.getTopUpDaBalanceValue());

					offerFulfilment.setTopUpTempServiceClass(prepaidCxOfferMonitoring.getTopUpTempServiceClass());

					return offerFulfilment;
				} else if("ARPU".equals(prepaidCxOfferMonitoring.getEventType())){
					offerFulfilment.setOperatorId(prepaidCxOfferMonitoring.getOperatorId());
					offerFulfilment.setArpuType (prepaidCxOfferMonitoring.getArpuType());
					offerFulfilment.setArpuSelectedTopUpCode(prepaidCxOfferMonitoring.getArpuSelectedTopUpCode());
					offerFulfilment.setArpuOp(prepaidCxOfferMonitoring.getArpuOp());
					offerFulfilment.setArpuValue(prepaidCxOfferMonitoring.getArpuValue());
					return offerFulfilment;
				}else if("Usage".equals(prepaidCxOfferMonitoring.getEventType())){
					offerFulfilment.setUsageServiceType(prepaidCxOfferMonitoring.getUsageServiceType());
					offerFulfilment.setCountryCode(prepaidCxOfferMonitoring.getCountryCode());
					offerFulfilment.setUsageType(prepaidCxOfferMonitoring.getUsageType());
					offerFulfilment.setUsageOperator(prepaidCxOfferMonitoring.getUsageOp());
					offerFulfilment.setUsageValue(prepaidCxOfferMonitoring.getUsageValue());
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

	public List<ResponSysProgram> listProgram() {
		List<ResponSysProgram> list = new ArrayList<>();
		for(PrepaidCxOfferConfig d : prepaidCxOfferConfigRepository.findAll()){
			String programName = d.getOverallOfferName();
			list.add(new ResponSysProgram(String.valueOf(d.getProgramId()), programName));
		}
		return list;
	}

	public List<PromoCode> listOfferType() {
		List<PromoCode> list  = promoCodeRepository.findEligiblePromo();
		list.add(new PromoCode("1", "DA Offer", "DA"));
		list.add(new PromoCode("2", "OMS Offer", "OMS"));

		return list;
	}


	public List<Country> listCountry(){
		return countryRepository.findAll();
	}

}