package com.dev.prepaid.controller;

import javax.persistence.EntityNotFoundException;

import com.dev.prepaid.domain.*;
import com.dev.prepaid.repository.PrepaidCxOfferConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.prepaid.model.configuration.Config;
import com.dev.prepaid.model.configuration.ServiceInstanceConfiguration;
import com.dev.prepaid.service.OfferService;
import com.dev.prepaid.util.GsonUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/")
public class UIController {
	
	@Autowired
	private PrepaidCxOfferConfigRepository prepaidCxOfferConfigRepository;
	
	@Autowired
	private OfferService offerService;
	
	@GetMapping(value = "test")
	public String test(Model model){		
		return "test-page";
	}

	@GetMapping(value = "configureDummy")
	public String configureDummy(Model model){
		Config config = Config.builder()
				.notification(true)
				.build();
		
		model.addAttribute("status", "CREATED");
		model.addAttribute("instanceId", "63483226-49f3-48a2-8720-4f4c47021e32");
		model.addAttribute("offers", new Config());
        return "config";
	}

	@PostMapping(value = "configure")
    public String configure(@RequestBody ServiceInstanceConfiguration serviceInstance, Model model){
		log.info("configure call");
		log.info(GsonUtils.deserializeObjectToJSON(serviceInstance));

		try {
			String offerBucketId = null;
			String offerBucketCode = null;
			String offerCampaignName = null;
			//if already exist
			PrepaidCxOfferConfig instance = prepaidCxOfferConfigRepository.findOneByInstanceIdAndDeletedDateIsNull(serviceInstance.getUuid());
			if(instance == null) {
				throw new EntityNotFoundException("Instance id not found " + serviceInstance.getUuid());
			}
	
			instance.setProgramId(serviceInstance.getAssetId());
			prepaidCxOfferConfigRepository.save(instance);
			
			log.debug("id : {}", serviceInstance.getUuid());
			

//			if(instance.getCampaignOfferType() != null && instance.getCampaignOfferType().equalsIgnoreCase("oms")) {
//				PrepaidOmsOfferCampaign offer = offerService.getOmsOfferCampaign(instance.getCampaignOfferId());
//				PrepaidOmsOfferBucket bucket = offerService.getOmsOfferBucket(offer.getOfferId());
//				offerCampaignName = offer.getName();
//				offerBucketId = "OMS|"+bucket.getId();
//				offerBucketCode = bucket.getCode();
//			}
//
//			if(instance.getCampaignOfferType() != null && instance.getCampaignOfferType().equalsIgnoreCase("da")) {
//				PrepaidDaOfferCampaign offer = offerService.getDaOfferCampaign(instance.getCampaignOfferId());
//				PrepaidDaOfferBucket bucket = offerService.getDaOfferBucket(offer.getOfferId());
//				offerCampaignName = offer.getName();
//				offerBucketId = "DA|"+bucket.getId();
//				offerBucketCode = bucket.getCode();
//
//			}
			
//			model.addAttribute("status", instance.getStatus());
			model.addAttribute("instanceId", instance.getInstanceId());
//			model.addAttribute("uuid", GUIDUtil.generateGUID());
//			model.addAttribute("offers", new Config().builder()
////					.offerType(instance.getCampaignOfferType())
//
//					.offerBucketId(offerBucketId)
//					.offerBucketCode(offerBucketCode)
//					.offerCampaignId(instance.getCampaignOfferId())
//					.offerCampaignName(offerCampaignName)
//
//					.notification(instance.getNotification())
//
//					.build());
	        return "config";
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
	        return "configDefault";
		}
    }
	
	
}
