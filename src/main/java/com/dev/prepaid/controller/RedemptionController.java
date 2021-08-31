package com.dev.prepaid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dev.prepaid.model.redemption.RedemptionResponse;
import com.dev.prepaid.service.RedemptionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/redemption/" , produces = MediaType.TEXT_PLAIN_VALUE)
public class RedemptionController {
	
	@Autowired
	private RedemptionService redemptionService;
//	
//	@GetMapping(value = "offerDetail")
//    public PrepaidCampaignOfferDetailDTO offerDetail(
////    		@RequestParam(value = "bucketName", required = false) String bucketName,
//    		@RequestParam(value = "bucketOfferId", required = false) String bucketOfferId,
//    		@RequestParam(value = "campaignOfferId", required = false) String campaignOfferId) throws Exception{
//		
//		String offerBucketType = AppUtil.stringTokenizer(bucketOfferId, "|").get(0);
//		bucketOfferId = AppUtil.stringTokenizer(bucketOfferId, "|").get(1);
//
//		log.debug("offerBucketType: {}",offerBucketType);
//		log.debug("offerId 	: {}",bucketOfferId);
//		log.debug("campaignOfferId: {}",campaignOfferId);
//		
//		if(offerBucketType.equalsIgnoreCase("OMS")) {
//			PrepaidOmsOfferCampaign prepaidOmsOfferCampaign = offerService.getOmsOfferCampaign(Long.parseLong(campaignOfferId));
//			PrepaidOmsOfferBucket prepaidOmsOfferBucket = offerService.getOmsOfferBucket(prepaidOmsOfferCampaign.getOfferId());
//			return PrepaidCampaignOfferDetailDTO.builder()
//					.offerBucketType(offerBucketType) //OMS
//					.offerName(prepaidOmsOfferCampaign.getName())
//					.offerId(Long.parseLong(prepaidOmsOfferBucket.getCode()))
//					.offerType(prepaidOmsOfferBucket.getType()) //timebased or accountbased
//					.description(prepaidOmsOfferCampaign.getDescription())
//					.value(prepaidOmsOfferCampaign.getValue())
//					.valueUnit(prepaidOmsOfferCampaign.getValueUnit())
//					.valueToDeductFromMa(prepaidOmsOfferCampaign.getValueToDeductFromMa())
//					.counterId(prepaidOmsOfferCampaign.getCounterId())
//					.thresholdId(prepaidOmsOfferCampaign.getThresholdId())
//					.thresholdValue(prepaidOmsOfferCampaign.getThresholdValue())
//					.thresholdValueUnit(prepaidOmsOfferCampaign.getThresholdValueUnit())
//					.startDate(DateUtil.dateToString(prepaidOmsOfferCampaign.getStartDate(), "yyyy-MMM-dd"))
//					.endDate(DateUtil.dateToString(prepaidOmsOfferCampaign.getEndDate(), "yyyy-MMM-dd"))
//					.action(prepaidOmsOfferCampaign.getAction())
//					.bucketName(prepaidOmsOfferBucket.getDescription())
//					.offerBucketId(offerBucketType.concat("|").concat(bucketOfferId))
//					.offerBucketType(offerBucketType)
//					.offerCampaignId(Long.valueOf(campaignOfferId))
//					.offerCampaignName(prepaidOmsOfferCampaign.getName())
//					.build();
//			
//
//		}
//		
//		if(offerBucketType.equalsIgnoreCase("DA")) {
//			PrepaidDaOfferBucket prepaidDaOfferBucket = offerService.getDaOfferBucket(Long.valueOf(bucketOfferId));
//			PrepaidDaOfferCampaign prepaidDaOfferCampaign = offerService.getDaOfferCampaign(Long.parseLong(campaignOfferId));
//			return PrepaidCampaignOfferDetailDTO.builder()
//					.offerName(prepaidDaOfferCampaign.getName())
//					.bucketName(prepaidDaOfferBucket.getDescription())
//					.description(prepaidDaOfferCampaign.getDescription())
//					.value(prepaidDaOfferCampaign.getValue())
//					.valueUnit(prepaidDaOfferCampaign.getValueUnit())
//					.valueCap(prepaidDaOfferCampaign.getValueCap())
//					.valueToDeductFromMa(prepaidDaOfferCampaign.getValueToDeductFromMa())
//					.valueValidityInDays(prepaidDaOfferCampaign.getValueValidityInDays())
//					.startDate(DateUtil.dateToString(prepaidDaOfferCampaign.getStartDate(), "yyyy-MMM-dd"))
//					.endDate(DateUtil.dateToString(prepaidDaOfferCampaign.getEndDate(), "yyyy-MMM-dd"))
//					.action(prepaidDaOfferCampaign.getAction())
//					.offerBucketId(offerBucketType.concat("|").concat(bucketOfferId))
//					.offerBucketType(offerBucketType)
//					.offerCampaignId(Long.valueOf(campaignOfferId))
//					.offerCampaignName(prepaidDaOfferCampaign.getName())
//					.build();
//		}
//		
//		return new PrepaidCampaignOfferDetailDTO();
//	}
//    		
//	@GetMapping(value = "bucketDetail")
//    public PrepaidBucketDetailDTO bucketDetail (
//    		@RequestParam(value = "bucketId", required = false) String bucketId2) throws Exception{
//		
//		log.debug("bucketId: {}",bucketId2);
//		
//		String bucketType = AppUtil.stringTokenizer(bucketId2, "|").get(0);
//		String bucketId = AppUtil.stringTokenizer(bucketId2, "|").get(1);
//		
//		if(bucketType.equalsIgnoreCase("OMS")) {
//			PrepaidOmsOfferBucket prepaidOmsOfferBucket = offerService.getOmsOfferBucket(Long.parseLong(bucketId));
//			return PrepaidBucketDetailDTO.builder()
//					.bucketName(prepaidOmsOfferBucket.getCode())
//					.offerType(prepaidOmsOfferBucket.getType())
//					.counterId(prepaidOmsOfferBucket.getCounterId())
//					.thresholdId(prepaidOmsOfferBucket.getThresholdId())
//					.build();
//		}
//		
//		if(bucketType.equalsIgnoreCase("DA")) {
//			PrepaidDaOfferBucket prepaidDaOfferBucket = offerService.getDaOfferBucket(Long.parseLong(bucketId));
//			return PrepaidBucketDetailDTO.builder()
//					.bucketName(prepaidDaOfferBucket.getCode())
//					.build();
//		}
//		
//		return new PrepaidBucketDetailDTO();
//	}
//	
//	@GetMapping(value = "offerBucket")
//    public List<DataOffer> offerBucketList(
//    		@RequestParam(value = "search", required = false) String query, 
//    		@RequestParam(value = "offerType", required = false) String offerType) throws ParseException {
//		log.info("queryBucket : {}",query);
//		
//		List<DataOffer> listBucket = new ArrayList<DataOffer>();
//		
//		if (offerType == null || offerType.isEmpty()) {
//			
//			if (query == null || query.isEmpty()) {
//
//				listBucket.addAll(offerService.listOmsOfferBucket()
//								    		.stream()
//								            .map(this::mapOmsBucketToOffer)
//								            .collect(Collectors.toList()));
//				listBucket.addAll(offerService.listDaOfferBucket()
//											.stream()
//											.map(this::mapDaBucketToOffer)
//											.collect(Collectors.toList()));
//			}else {
//
//	        	listBucket.addAll(offerService.listOmsOfferBucket()
//											.stream()
//											.filter(p -> p.getCode().toLowerCase().contains(query))
//											.map(this::mapOmsBucketToOffer)
//											.collect(Collectors.toList()));
//	        	listBucket.addAll(offerService.listDaOfferBucket()
//											.stream()
//											.filter(p -> p.getCode().toLowerCase().contains(query))
//											.map(this::mapDaBucketToOffer)
//											.collect(Collectors.toList()));
//			}
//        } else if (offerType.equalsIgnoreCase("DA")) {
//        	
//        	if (query == null || query.isEmpty()) {
//
//            	listBucket.addAll(offerService.listDaOfferBucket()
//    					.stream()
//    					.map(this::mapDaBucketToOffer)
//    					.collect(Collectors.toList()));
//        	} else {
//
//            	listBucket.addAll(offerService.listDaOfferBucket()
//    					.stream()
//    					.filter(p -> p.getCode().toLowerCase().contains(query))
//    					.map(this::mapDaBucketToOffer)
//    					.collect(Collectors.toList()));
//        	}
//        	
//        } else if (offerType.equalsIgnoreCase("OMS")) {
//        	if (query == null || query.isEmpty()) {
//
//            	listBucket.addAll(offerService.listOmsOfferBucket()
//    					.stream()
//    					.map(this::mapOmsBucketToOffer)
//    					.collect(Collectors.toList()));
//        	} else {
//
//            	listBucket.addAll(offerService.listOmsOfferBucket()
//    					.stream()
//    					.filter(p -> p.getCode().toLowerCase().contains(query))
//    					.map(this::mapOmsBucketToOffer)
//    					.collect(Collectors.toList()));
//        	}
//        	
//        } else {
//        }
//		
//		return listBucket;
//    }
//	
//	@GetMapping(value = "offerCampaign")
//    public List<DataOffer> offerCampaignList(
//    		@RequestParam(value = "offerId", required = false) String offerId,  
//    		@RequestParam(value = "search", required = false) String query) throws ParseException {
//		
//		if(offerId.isBlank()) {
//			return null;
//		}
//		
//		String offerBucketType = AppUtil.stringTokenizer(offerId, "|").get(0);
//		offerId = AppUtil.stringTokenizer(offerId, "|").get(1);
//		log.debug("offerBucketType: {}",offerBucketType);
//		log.debug("offerId 	: {}",offerId);
//		log.debug("queryOffer: {}",query);
//		
//		
//		
//		if (query == null || query.isEmpty()) {
//			if(offerBucketType.equalsIgnoreCase("OMS")) {
//				return offerService.listOmsOfferCampaign(Long.parseLong(offerId))
//						.stream()
//						.map(this::mapOmsCampaignToOffer)
//						.collect(Collectors.toList());	
//				
//			}else if(offerBucketType.equalsIgnoreCase("DA")) {
//				return offerService.listDaOfferCampaign(Long.parseLong(offerId))
//						.stream()
//						.map(this::mapDaCampaignToOffer)
//						.collect(Collectors.toList());
//				
//			}
//        }
//		
//		if(offerBucketType.equalsIgnoreCase("OMS")) {
//			return offerService.listOmsOfferCampaign(Long.parseLong(offerId))
//					.stream()
//					.filter(p -> p.getName().toLowerCase().contains(query))
//					.map(this::mapOmsCampaignToOffer)
//					.collect(Collectors.toList());	
//			
//		}else if(offerBucketType.equalsIgnoreCase("DA")) {
//			return offerService.listDaOfferCampaign(Long.parseLong(offerId))
//					.stream()
//					.filter(p -> p.getName().toLowerCase().contains(query))
//					.map(this::mapDaCampaignToOffer)
//					.collect(Collectors.toList());
//			
//		}
//
//		return null;			
//    }
//	
//	private DataOffer mapOmsBucketToOffer(PrepaidOmsOfferBucket oms) {
//		//OMS|123
//        return DataOffer.builder()
//                        .id("OMS|"+oms.getId())
//                        .text(oms.getCode())
//                        .slug(oms.getCode())
//                        .build();
//    }
//	private DataOffer mapOmsCampaignToOffer(PrepaidOmsOfferCampaign oms) {
//        return DataOffer.builder()
//                        .id(oms.getId().toString())
//                        .text(oms.getName())
//                        .slug(oms.getName())
//                        .build();
//    }
//	
//	
//	private DataOffer mapDaBucketToOffer(PrepaidDaOfferBucket da) {
//		//OMS|123
//        return DataOffer.builder()
//                        .id("DA|"+da.getId())
//                        .text(da.getCode())
//                        .slug(da.getCode())
//                        .build();
//    }
//	private DataOffer mapDaCampaignToOffer(PrepaidDaOfferCampaign da) {
//        return DataOffer.builder()
//                        .id(da.getId().toString())
//                        .text(da.getName())
//                        .slug(da.getName())
//                        .build();
//    }
//		
//	@GetMapping(value = "evictCache")
//    public void evict(){
//		offerService.evictAllCaches();
//	}
//    		
//    @GetMapping(value = "offerSelection")
//	public List<PrepaidCampaignOfferDetailDTO> getOfferSelection(@RequestParam(value = "instanceId", required = false) String instanceId) throws Exception {
//		List<PrepaidCampaignOfferDetailDTO> list = new ArrayList<>();
//		List<PrepaidCxOfferSelection>  data = offerService.getOfferSelection(instanceId);
//		for (PrepaidCxOfferSelection prepaidCxOfferSelection: data){
//			PrepaidCampaignOfferDetailDTO offerDetailDTO = offerDetail(
//					prepaidCxOfferSelection.getOfferBucketType().concat("|").concat(prepaidCxOfferSelection.getOfferBucketId()),
//					String.valueOf(prepaidCxOfferSelection.getOfferId()));
////			offerDetailDTO.setOfferBucketId(prepaidCxOfferSelection.getOfferBucketType().concat("|").concat(prepaidCxOfferSelection.getOfferBucketId()));
////			offerDetailDTO.setOfferBucketType(prepaidCxOfferSelection.getOfferBucketType());
////			offerDetailDTO.setOfferCampaignName(prepaidCxOfferSelection.getOfferType());
////			offerDetailDTO.setOfferCampaignId(Long.valueOf(prepaidCxOfferSelection.getOfferId()));
//
//			list.add(offerDetailDTO);
//		}
//		return  list;
//	}
//	@GetMapping(value = "offerEligibility")
//	public PrepaidCxOfferEligibility getOfferEligibility(@RequestParam(value = "instanceId", required = false) String instanceId){
//		return offerService.getOfferEligibility(instanceId);
//	}
//	@GetMapping(value = "offerMonitoring")
//	public OfferFulfilment getOfferMonitoring(@RequestParam(value = "instanceId", required = false) String instanceId){
//		return offerService.getOfferMonitoring(instanceId);
//	}
//	@GetMapping(value = "offerRedemption")
//	public PrepaidCxOfferRedemption getOfferRedemption(@RequestParam(value = "instanceId", required = false) String instanceId){
//		return offerService.getOfferRedemption(instanceId);
//	}
//	
	
	@RequestMapping(value = "processRedemption",
			method = RequestMethod.POST)
	public ResponseEntity<String> processRedemption(@RequestBody String msisdn,@RequestBody String evenType){
		redemptionService.processByCall(msisdn,evenType);
		
		
		return ResponseEntity.status(HttpStatus.OK).body("Response:"+msisdn +"redemption done");
		
//		return  ResponseEntity
//                .status(HttpStatus.OK)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body("Response:"+msisdn +"redemption done");
	}
	
	@GetMapping("test")
    ResponseEntity<String> findAll ()
    {
			
        String hello="Response Hello..";
        return  ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(hello);
    }
	
	
	@RequestMapping(value = "processRedemption2",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RedemptionResponse> processRedemption2(@RequestBody String msisdn){
		
		RedemptionResponse response = RedemptionResponse.builder()
				.successful(true)
				.content("Msisd:"+msisdn)
				.errorMessage("")
				.build();
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
		
//		return  ResponseEntity
//                .status(HttpStatus.OK)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body("Response:"+msisdn +"redemption done");
	}
	
}
