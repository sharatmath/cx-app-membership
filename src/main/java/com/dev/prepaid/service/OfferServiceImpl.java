package com.dev.prepaid.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.dev.prepaid.domain.PrepaidDaOfferBucket;
import com.dev.prepaid.domain.PrepaidDaOfferCampaign;
import com.dev.prepaid.domain.PrepaidOmsOfferBucket;
import com.dev.prepaid.domain.PrepaidOmsOfferCampaign;
import com.dev.prepaid.repository.PrepaidDaOfferBucketRepository;
import com.dev.prepaid.repository.PrepaidDaOfferCampaignRepository;
import com.dev.prepaid.repository.PrepaidOmsOfferBucketRepository;
import com.dev.prepaid.repository.PrepaidOmsOfferCampaignRepository;
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



}
