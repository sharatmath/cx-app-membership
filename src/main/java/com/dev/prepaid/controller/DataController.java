package com.dev.prepaid.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.prepaid.constant.Constant;
import com.dev.prepaid.domain.Country;
import com.dev.prepaid.domain.OverallOfferName;
import com.dev.prepaid.domain.PrepaidCxOfferAdvanceFilter;
import com.dev.prepaid.domain.PrepaidCxOfferEligibility;
import com.dev.prepaid.domain.PrepaidCxOfferEventCondition;
import com.dev.prepaid.domain.PrepaidCxOfferRedemption;
import com.dev.prepaid.domain.PrepaidDaOfferBucket;
import com.dev.prepaid.domain.PrepaidDaOfferCampaign;
import com.dev.prepaid.domain.PrepaidOmsOfferBucket;
import com.dev.prepaid.domain.PrepaidOmsOfferCampaign;
import com.dev.prepaid.domain.PromoCode;
import com.dev.prepaid.model.AdvFltrTblDTO;
import com.dev.prepaid.model.DataOffer;
import com.dev.prepaid.model.PrepaidBucketDetailDTO;
import com.dev.prepaid.model.PrepaidCampaignOfferDetailDTO;
import com.dev.prepaid.model.configuration.EventCondition;
import com.dev.prepaid.model.configuration.OfferFulfilment;
import com.dev.prepaid.model.configuration.OfferPromoCode;
import com.dev.prepaid.model.configuration.OfferRedemption;
import com.dev.prepaid.model.configuration.OfferSelection;
import com.dev.prepaid.model.configuration.ResponSysProgram;
import com.dev.prepaid.model.request.DataControllRequest;
import com.dev.prepaid.model.request.GetAccumulatedTopups;
import com.dev.prepaid.model.request.GetPackageFrequency;
import com.dev.prepaid.model.request.GetTopupFrequency;
import com.dev.prepaid.model.request.IsPaidTopupInLastXDays;
import com.dev.prepaid.service.IPrepaidCxOfferAdvanceFilterService;
import com.dev.prepaid.service.OfferService;
import com.dev.prepaid.util.AppUtil;
import com.dev.prepaid.util.DateUtil;
import com.dev.prepaid.util.OperationUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/data/")
public class DataController {

	@Autowired
	private OfferService offerService;

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/*
	 * @Autowired private PrepaidCxOfferAdvanceFilterRepository
	 * prepaidCxOfferAdvanceFilterRepository;
	 */

	@Autowired
	IPrepaidCxOfferAdvanceFilterService prepaidCxOfferAdvanceFilterService;

	@GetMapping(value = "offerDetail")
	public PrepaidCampaignOfferDetailDTO offerDetail(
//    		@RequestParam(value = "bucketName", required = false) String bucketName,
			@RequestParam(value = "bucketOfferId", required = false) String bucketOfferId,
			@RequestParam(value = "campaignOfferId", required = false) String campaignOfferId) throws Exception {

		String offerBucketType = AppUtil.stringTokenizer(bucketOfferId, "|").get(0);
		bucketOfferId = AppUtil.stringTokenizer(bucketOfferId, "|").get(1);

		log.debug("offerBucketType: {}", offerBucketType);
		log.debug("offerId 	: {}", bucketOfferId);
		log.debug("campaignOfferId: {}", campaignOfferId);

		if (offerBucketType.equalsIgnoreCase("OMS")) {
			PrepaidOmsOfferCampaign prepaidOmsOfferCampaign = offerService
					.getOmsOfferCampaign(Long.parseLong(campaignOfferId));
			PrepaidOmsOfferBucket prepaidOmsOfferBucket = offerService
					.getOmsOfferBucket(prepaidOmsOfferCampaign.getOfferId());
			return PrepaidCampaignOfferDetailDTO.builder().offerBucketType(offerBucketType) // OMS
					.offerName(prepaidOmsOfferCampaign.getName())
					.offerId(Long.parseLong(prepaidOmsOfferBucket.getCode())).offerType(prepaidOmsOfferBucket.getType()) // timebased
																															// or
																															// accountbased
					.description(prepaidOmsOfferCampaign.getDescription()).value(prepaidOmsOfferCampaign.getValue())
					.valueUnit(prepaidOmsOfferCampaign.getValueUnit())
					.valueToDeductFromMa(prepaidOmsOfferCampaign.getValueToDeductFromMa())
					.counterId(prepaidOmsOfferCampaign.getCounterId())
					.thresholdId(prepaidOmsOfferCampaign.getThresholdId())
					.thresholdValue(prepaidOmsOfferCampaign.getThresholdValue())
					.thresholdValueUnit(prepaidOmsOfferCampaign.getThresholdValueUnit())
					.startDate(DateUtil.dateToString(prepaidOmsOfferCampaign.getStartDate(), "yyyy-MMM-dd"))
					.endDate(DateUtil.dateToString(prepaidOmsOfferCampaign.getEndDate(), "yyyy-MMM-dd"))
					.action(prepaidOmsOfferCampaign.getAction()).bucketName(prepaidOmsOfferBucket.getDescription())
					.offerBucketId(offerBucketType.concat("|").concat(bucketOfferId)).offerBucketType(offerBucketType)
					.offerCampaignId(Long.valueOf(campaignOfferId)).offerCampaignName(prepaidOmsOfferCampaign.getName())
					.dayHourMinute(String.valueOf(prepaidOmsOfferCampaign.getDay()) + " Day "
							+ String.valueOf(prepaidOmsOfferCampaign.getHour()) + " Hour "
							+ String.valueOf(prepaidOmsOfferCampaign.getMinute()) + " Minute ")
					.build();

		}

		if (offerBucketType.equalsIgnoreCase("DA")) {
			PrepaidDaOfferBucket prepaidDaOfferBucket = offerService.getDaOfferBucket(Long.valueOf(bucketOfferId));
			PrepaidDaOfferCampaign prepaidDaOfferCampaign = offerService
					.getDaOfferCampaign(Long.parseLong(campaignOfferId));
			return PrepaidCampaignOfferDetailDTO.builder().offerName(prepaidDaOfferCampaign.getName())
					.bucketName(prepaidDaOfferBucket.getDescription())
					.description(prepaidDaOfferCampaign.getDescription()).value(prepaidDaOfferCampaign.getValue())
					.valueUnit(prepaidDaOfferCampaign.getValueUnit()).valueCap(prepaidDaOfferCampaign.getValueCap())
					.valueToDeductFromMa(prepaidDaOfferCampaign.getValueToDeductFromMa())
					.valueValidityInDays(prepaidDaOfferCampaign.getValueValidityInDays())
					.startDate(DateUtil.dateToString(prepaidDaOfferCampaign.getStartDate(), "yyyy-MMM-dd"))
					.endDate(DateUtil.dateToString(prepaidDaOfferCampaign.getEndDate(), "yyyy-MMM-dd"))
					.action(prepaidDaOfferCampaign.getAction())
					.offerBucketId(offerBucketType.concat("|").concat(bucketOfferId)).offerBucketType(offerBucketType)
					.offerCampaignId(Long.valueOf(campaignOfferId)).offerCampaignName(prepaidDaOfferCampaign.getName())
					.build();
		}

		return new PrepaidCampaignOfferDetailDTO();
	}

	@GetMapping(value = "bucketDetail")
	public PrepaidBucketDetailDTO bucketDetail(@RequestParam(value = "bucketId", required = false) String bucketId2)
			throws Exception {

		log.debug("bucketId: {}", bucketId2);

		String bucketType = AppUtil.stringTokenizer(bucketId2, "|").get(0);
		String bucketId = AppUtil.stringTokenizer(bucketId2, "|").get(1);

		if (bucketType.equalsIgnoreCase("OMS")) {
			PrepaidOmsOfferBucket prepaidOmsOfferBucket = offerService.getOmsOfferBucket(Long.parseLong(bucketId));
			return PrepaidBucketDetailDTO.builder().bucketName(prepaidOmsOfferBucket.getCode())
					.offerType(prepaidOmsOfferBucket.getType()).counterId(prepaidOmsOfferBucket.getCounterId())
					.thresholdId(prepaidOmsOfferBucket.getThresholdId()).build();
		}

		if (bucketType.equalsIgnoreCase("DA")) {
			PrepaidDaOfferBucket prepaidDaOfferBucket = offerService.getDaOfferBucket(Long.parseLong(bucketId));
			return PrepaidBucketDetailDTO.builder().bucketName(prepaidDaOfferBucket.getCode()).build();
		}

		return new PrepaidBucketDetailDTO();
	}

	@GetMapping(value = "offerBucket")
	public List<DataOffer> offerBucketList(@RequestParam(value = "search", required = false) String query,
			@RequestParam(value = "offerType", required = false) String offerType) throws ParseException {
		log.info("queryBucket : {}", query);

		List<DataOffer> listBucket = new ArrayList<DataOffer>();

		if (offerType == null || offerType.isEmpty()) {

			if (query == null || query.isEmpty()) {

				listBucket.addAll(offerService.listOmsOfferBucket().stream().map(this::mapOmsBucketToOffer)
						.collect(Collectors.toList()));
				listBucket.addAll(offerService.listDaOfferBucket().stream().map(this::mapDaBucketToOffer)
						.collect(Collectors.toList()));
			} else {

				listBucket.addAll(offerService.listOmsOfferBucket().stream()
						.filter(p -> p.getCode().toLowerCase().contains(query)).map(this::mapOmsBucketToOffer)
						.collect(Collectors.toList()));
				listBucket.addAll(
						offerService.listDaOfferBucket().stream().filter(p -> p.getCode().toLowerCase().contains(query))
								.map(this::mapDaBucketToOffer).collect(Collectors.toList()));
			}
		} else if (offerType.equalsIgnoreCase("DA")) {

			if (query == null || query.isEmpty()) {

				listBucket.addAll(offerService.listDaOfferBucket().stream().map(this::mapDaBucketToOffer)
						.collect(Collectors.toList()));
			} else {

				listBucket.addAll(
						offerService.listDaOfferBucket().stream().filter(p -> p.getCode().toLowerCase().contains(query))
								.map(this::mapDaBucketToOffer).collect(Collectors.toList()));
			}

		} else if (offerType.equalsIgnoreCase("OMS")) {
			if (query == null || query.isEmpty()) {

				listBucket.addAll(offerService.listOmsOfferBucket().stream().map(this::mapOmsBucketToOffer)
						.collect(Collectors.toList()));
			} else {

				listBucket.addAll(offerService.listOmsOfferBucket().stream()
						.filter(p -> p.getCode().toLowerCase().contains(query)).map(this::mapOmsBucketToOffer)
						.collect(Collectors.toList()));
			}

		} else if (offerType.equalsIgnoreCase("MA")) {
			if (query == null || query.isEmpty()) {

				listBucket.addAll(offerService.listOmsOfferBucket().stream().map(this::mapOmsBucketToOffer)
						.collect(Collectors.toList()));
			} else {

				listBucket.addAll(offerService.listOmsOfferBucket().stream()
						.filter(p -> p.getCode().toLowerCase().contains(query)).map(this::mapOmsBucketToOffer)
						.collect(Collectors.toList()));
			}

		} else {
		}

		return listBucket;
	}

	@GetMapping(value = "offerCampaign")
	public List<DataOffer> offerCampaignList(@RequestParam(value = "offerId", required = false) String offerId,
			@RequestParam(value = "search", required = false) String query) throws ParseException {

		if (offerId.isBlank()) {
			return null;
		}

		String offerBucketType = AppUtil.stringTokenizer(offerId, "|").get(0);
		offerId = AppUtil.stringTokenizer(offerId, "|").get(1);
		log.debug("offerBucketType: {}", offerBucketType);
		log.debug("offerId 	: {}", offerId);
		log.debug("queryOffer: {}", query);

		if (query == null || query.isEmpty()) {
			if (offerBucketType.equalsIgnoreCase("OMS")) {
				return offerService.listOmsOfferCampaign(Long.parseLong(offerId)).stream()
						.map(this::mapOmsCampaignToOffer).collect(Collectors.toList());

			} else if (offerBucketType.equalsIgnoreCase("DA")) {
				return offerService.listDaOfferCampaign(Long.parseLong(offerId)).stream()
						.map(this::mapDaCampaignToOffer).collect(Collectors.toList());

			}
		}

		if (offerBucketType.equalsIgnoreCase("OMS")) {
			return offerService.listOmsOfferCampaign(Long.parseLong(offerId)).stream()
					.filter(p -> p.getName().toLowerCase().contains(query)).map(this::mapOmsCampaignToOffer)
					.collect(Collectors.toList());

		} else if (offerBucketType.equalsIgnoreCase("DA")) {
			return offerService.listDaOfferCampaign(Long.parseLong(offerId)).stream()
					.filter(p -> p.getName().toLowerCase().contains(query)).map(this::mapDaCampaignToOffer)
					.collect(Collectors.toList());

		}

		return null;
	}

	private DataOffer mapOmsBucketToOffer(PrepaidOmsOfferBucket oms) {
		// OMS|123
		return DataOffer.builder().id("OMS|" + oms.getId()).text(oms.getCode()).slug(oms.getCode()).build();
	}

	private DataOffer mapOmsCampaignToOffer(PrepaidOmsOfferCampaign oms) {
		return DataOffer.builder().id(oms.getId().toString()).text(oms.getName()).slug(oms.getName()).build();
	}

	private DataOffer mapDaBucketToOffer(PrepaidDaOfferBucket da) {
		// OMS|123
		return DataOffer.builder().id("DA|" + da.getId()).text(da.getCode()).slug(da.getCode()).build();
	}

	private DataOffer mapDaCampaignToOffer(PrepaidDaOfferCampaign da) {
		return DataOffer.builder().id(da.getId().toString()).text(da.getName()).slug(da.getName()).build();
	}

	@GetMapping(value = "evictCache")
	public void evict() {
		offerService.evictAllCaches();
	}

	@GetMapping(value = "offerPromoCode")
	public OfferPromoCode getOfferPromoCode(@RequestParam(value = "instanceId", required = false) String instanceId)
			throws Exception {
		return offerService.getOfferPromoCode(instanceId);
	}

	@GetMapping(value = "offerSelection")
	public List<PrepaidCampaignOfferDetailDTO> getOfferSelection(
			@RequestParam(value = "instanceId", required = false) String instanceId) throws Exception {
		List<PrepaidCampaignOfferDetailDTO> list = new ArrayList<>();
		List<OfferSelection> data = offerService.getOfferSelection(instanceId);
		for (OfferSelection prepaidCxOfferSelection : data) {
			log.info("{}", prepaidCxOfferSelection);
			PrepaidCampaignOfferDetailDTO offerDetailDTO = new PrepaidCampaignOfferDetailDTO();
			offerDetailDTO = offerDetail(
					prepaidCxOfferSelection.getOfferBucketType().concat("|")
							.concat(prepaidCxOfferSelection.getOfferBucketId()),
					String.valueOf(prepaidCxOfferSelection.getOfferId()));
//			offerDetailDTO.setOfferBucketId(prepaidCxOfferSelection.getOfferBucketType().concat("|").concat(prepaidCxOfferSelection.getOfferBucketId()));
//			offerDetailDTO.setOfferBucketType(prepaidCxOfferSelection.getOfferBucketType());
//			offerDetailDTO.setOfferCampaignName(prepaidCxOfferSelection.getOfferType());
//			offerDetailDTO.setOfferCampaignId(Long.valueOf(prepaidCxOfferSelection.getOfferId()));
//			offerDetailDTO.setSmsCampaignName(prepaidCxOfferSelection.getSmsCampaignName());
//			offerDetailDTO.setOfferType(prepaidCxOfferSelection.getOfferType());
//			offerDetailDTO.setPromoCodeList(prepaidCxOfferSelection.getPromoCodeList());
//			offerDetailDTO.setMessageText1(prepaidCxOfferSelection.getMessageText1());
//			offerDetailDTO.setMessageText2(prepaidCxOfferSelection.getMessageText2());
//			offerDetailDTO.setMessageText3(prepaidCxOfferSelection.getMessageText3());
//			offerDetailDTO.setMessageText4(prepaidCxOfferSelection.getMessageText4());
//			offerDetailDTO.setOverallOfferName(prepaidCxOfferSelection.getOverallOfferName());
			log.info("{}", offerDetailDTO);
			list.add(offerDetailDTO);
		}
		return list;
	}

	@GetMapping(value = "offerEligibility")
	public PrepaidCxOfferEligibility getOfferEligibility(
			@RequestParam(value = "instanceId", required = false) String instanceId) {
		return offerService.getOfferEligibility(instanceId);
	}

	@GetMapping(value = "offerMonitoring")
	public OfferFulfilment getOfferMonitoring(@RequestParam(value = "instanceId", required = false) String instanceId) {
		return offerService.getOfferMonitoring(instanceId);
	}

	@GetMapping(value = "offerRedemption")
	public OfferRedemption getOfferRedemption(@RequestParam(value = "instanceId", required = false) String instanceId) {
		PrepaidCxOfferRedemption prepaidCxOfferRedemption = offerService.getOfferRedemption(instanceId);
		if (prepaidCxOfferRedemption != null) {

			OfferRedemption offerRedemption = OfferRedemption.builder()
					.isDateRange(prepaidCxOfferRedemption.isDateRange()).isPeriod(prepaidCxOfferRedemption.isPeriod())
					.optPeriod(prepaidCxOfferRedemption.getOptPeriod())
					.smsCampaignName(prepaidCxOfferRedemption.getSmsCampaignName())
					.postSmsCampaignName(prepaidCxOfferRedemption.getPostSmsCampaignName())
					.optKeyword(prepaidCxOfferRedemption.getOptKeyword())
					.isFrequencyAndTime(prepaidCxOfferRedemption.getIsFrequencyAndTime())
					.isFrequencyOnly(prepaidCxOfferRedemption.getIsFrequencyOnly())
					.redemptionMethod(prepaidCxOfferRedemption.getRedemptionMethod())
					.recurringFrequencyDayOfMonth(prepaidCxOfferRedemption.getRecurringFrequencyDayOfMonth())
					.recurringFrequencyPeriodType(prepaidCxOfferRedemption.getRecurringFrequencyPeriodType())
					.recurringFrequencyValue(prepaidCxOfferRedemption.getRecurringFrequencyValue())
					.recurringFrequencyPeriodValue(prepaidCxOfferRedemption.getRecurringFrequencyPeriodValue())
					.totalRecurringFrequency(prepaidCxOfferRedemption.getTotalRecurringFrequency())
					.totalRedemptionPeriodEvery(prepaidCxOfferRedemption.getTotalRedemptionPeriodEvery())
					.totalRedemptionPeriodValue(prepaidCxOfferRedemption.getTotalRedemptionPeriodValue())
					.totalRedemptionPeriodType(prepaidCxOfferRedemption.getTotalRedemptionPeriodType())
					.dynamicVariable1(prepaidCxOfferRedemption.getDynamicVariable1())
					.dynamicVariable2(prepaidCxOfferRedemption.getDynamicVariable2())
					.dynamicVariable3(prepaidCxOfferRedemption.getDynamicVariable3())
					.dynamicVariable4(prepaidCxOfferRedemption.getDynamicVariable4())
					.dynamicVariable5(prepaidCxOfferRedemption.getDynamicVariable5())
					.isRedemptionCapOnly(prepaidCxOfferRedemption.getIsRedemptionCapOnly())
					.redemptionCapValue(prepaidCxOfferRedemption.getRedemptionCapValue())
					.isFrequencyOnly(prepaidCxOfferRedemption.getIsFrequencyOnly())
					.frequencyValue(prepaidCxOfferRedemption.getFrequencyValue())
					.timePeriodEvery(prepaidCxOfferRedemption.getTimePeriodEvery())
					.timePeriodType(prepaidCxOfferRedemption.getTimePeriodType())
					.timePeriodValue(prepaidCxOfferRedemption.getTimePeriodValue())
					.totalRecurringFrequency(prepaidCxOfferRedemption.getTotalRecurringFrequency())
					.recurringFrequencyDayOfMonth(prepaidCxOfferRedemption.getRecurringFrequencyDayOfMonth())
					.isRecurringFrequencyEachMonth(prepaidCxOfferRedemption.getIsRecurringFrequencyEachMonth())
					.recurringFrequencyPeriodValue(prepaidCxOfferRedemption.getRecurringFrequencyPeriodValue())
					.recurringFrequencyValue(prepaidCxOfferRedemption.getRecurringFrequencyValue())
					.isRecurringFrequencyAndPeriod(prepaidCxOfferRedemption.getIsRecurringFrequencyAndPeriod())
					.isRedemptionCapAndPeriod(prepaidCxOfferRedemption.getIsRedemptionCapAndPeriod()).build();

			try {
				log.info("DateUtil.fromDate {}", prepaidCxOfferRedemption);
				if (prepaidCxOfferRedemption.getOptEndDate() != null)
					offerRedemption.setOptEndDate(DateUtil.fromDate(prepaidCxOfferRedemption.getOptEndDate()));
				if (prepaidCxOfferRedemption.getOptStartDate() != null)
					offerRedemption.setOptStartDate(DateUtil.fromDate(prepaidCxOfferRedemption.getOptStartDate()));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			return offerRedemption;
		}

		return new OfferRedemption();
	}

	@GetMapping(value = "offerEventCondition")
	public EventCondition getOfferEventCondition(
			@RequestParam(value = "instanceId", required = false) String instanceId) {
		PrepaidCxOfferEventCondition prepaidCxOfferEventCondition = offerService.getOfferEventCondition(instanceId);
		if (prepaidCxOfferEventCondition != null) {

			EventCondition eventCondition = EventCondition.builder()
					.eventConditionName(prepaidCxOfferEventCondition.getEventConditionName())
					.eventConditionType(prepaidCxOfferEventCondition.getEventConditionType())
					.eventTypeUsages(prepaidCxOfferEventCondition.getEventTypeUsages())
					.eventUsagesOp(prepaidCxOfferEventCondition.getEventUsagesOp())
					.eventUsagesValue(prepaidCxOfferEventCondition.getEventUsagesValue())
					.arpuOp(prepaidCxOfferEventCondition.getArpuOp())
					.arpuType(prepaidCxOfferEventCondition.getArpuType())
					.arpuValue(prepaidCxOfferEventCondition.getArpuValue())
					.arpuSelectedTopUpCode(prepaidCxOfferEventCondition.getArpuSelectedTopUpCode())
					.topUpAccBalanceBeforeOp(prepaidCxOfferEventCondition.getTopUpAccBalanceBeforeOp())
					.topUpCode(prepaidCxOfferEventCondition.getTopUpCode())
					.topUpType(prepaidCxOfferEventCondition.getTopUpType())
					.topUpAccBalanceBeforeValue(prepaidCxOfferEventCondition.getTopUpAccBalanceBeforeValue())
					.topUpCurBalanceValue(prepaidCxOfferEventCondition.getTopUpCurBalanceValue())
					.topUpTransactionValue(prepaidCxOfferEventCondition.getTopUpTransactionValue())
					.chargedAmount(prepaidCxOfferEventCondition.getChargedAmount())
					.imei(prepaidCxOfferEventCondition.getImei())
					.aggregationPeriodDays(prepaidCxOfferEventCondition.getAggregationPeriodDays())
					.daBalanceOp(prepaidCxOfferEventCondition.getDaBalanceOp())
					.daChange(prepaidCxOfferEventCondition.getDaChange())
					.daBalanceValue(prepaidCxOfferEventCondition.getDaBalanceValue())
//					.daId(prepaidCxOfferEventCondition.getDaId()).build();

					.daId(prepaidCxOfferEventCondition.getDaId())
					.roamingFlag(prepaidCxOfferEventCondition.getRoamingFlag())
					.ratePlanId(prepaidCxOfferEventCondition.getRatePlanId()).build();
			try {
				log.info("getOfferEventCondition DateUtil.fromDate( {}", prepaidCxOfferEventCondition);
				if (prepaidCxOfferEventCondition.getCampaignEndDate() != null) {
					eventCondition
							.setCampaignEndDate(DateUtil.fromDate(prepaidCxOfferEventCondition.getCampaignEndDate()));
				}
				if (prepaidCxOfferEventCondition.getCampaignStartDate() != null) {
					eventCondition.setCampaignStartDate(
							DateUtil.fromDate(prepaidCxOfferEventCondition.getCampaignStartDate()));
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}

			return eventCondition;

		} else {
			return new EventCondition();
		}

	}

	@GetMapping(value = "listProgram")
	public List<ResponSysProgram> listProgram() {
		return offerService.listProgram();
	}

	@GetMapping(value = "listCountry")
	public List<Country> listCountry() {
		return offerService.listCountry();
	}

	@PostMapping(value = "offerMonitoringTrx")
	public ResponseEntity<String> offerMonitoringTrx(@RequestBody Map<String, Object> payload) {
		rabbitTemplate.convertAndSend(Constant.TOPIC_EXCHANGE_NAME_MEMBERSHIP,
				Constant.QUEUE_NAME_MEMBERSHIP_MONITORING, payload);
		log.info("{}", payload);
		return ResponseEntity.ok("Success");
	}

	@PostMapping(value = "offerMonitoringTrxBulk")
	public ResponseEntity<String> offerMonitoringTrxBulk(@RequestBody List<Map<String, Object>> payload) {
		rabbitTemplate.convertAndSend(Constant.TOPIC_EXCHANGE_NAME_MEMBERSHIP,
				Constant.QUEUE_NAME_MEMBERSHIP_MONITORING, payload);
		log.info("{}", payload);
		return ResponseEntity.ok("Success");
	}

	@GetMapping(value = "listTest")
	public List<Country> listTest() {
		return offerService.listCountry();
	}

	@GetMapping(value = "listOfferType")
	public List<PromoCode> listOfferType() {
		return offerService.listOfferType();
	}

	@GetMapping(value = "checkUniqueOverallOfferName")
	public OverallOfferName checkUniqueOverallOfferName(
			@RequestParam(value = "overallOfferName", required = true) String overallOfferName) throws ParseException {
		return offerService.checkOverallOfferName(overallOfferName);
	}

//	Saket

	@RequestMapping(value = { "/getMyQuery" }, method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<Map<String, Object>> getQuery(@RequestBody DataControllRequest request) {
		Map<String, Object> result = new HashMap<>();
		if (request == null) {

		}
//		GetTopupFrequencyMatchForXMonths
		if (request.getGetPackageFrequency() != null) {
			GetPackageFrequency pkgFrequency = request.getGetPackageFrequency();
			String packageFrequencyPackageName = pkgFrequency.getGetPackageFrequencyPackageName();
			int startMonth = pkgFrequency.getGetPackageFrequencyStartMonth();

			String sql = "SELECT * FROM F_TBL_TOPUP_MT_A " + " WHERE getPackageFrequencyStartMonth = " + startMonth
					+ " and " + "packageFrequencyPackageName = " + packageFrequencyPackageName;
			System.out.println(sql);
			if (!OperationUtil.isEmpty(sql) && sql != null) {
				result.put("GetPackageFrequency", sql);
			}
		}

//		GetTopupFrequency
		if (request.getGetTopupFrequency() != null) {
			GetTopupFrequency topupFrequency = request.getGetTopupFrequency();
			String topupFrequencyMatchForXMonthsPackageName = topupFrequency
					.getGetTopupFrequencyMatchForXMonthsPackageName();
//			int startMonth = pkgFrequency.getGetPackageFrequencyStartMonth();

			String sql = "SELECT * FROM F_TBL_TOPUP_MT_A " + " WHERE topupFrequencyMatchForXMonthsPackageName = "
					+ topupFrequencyMatchForXMonthsPackageName;
			System.out.println(sql);
			if (!OperationUtil.isEmpty(sql) && sql != null) {
				result.put("GetPackageFrequency", sql);
			}
		}
//		
//		if() {
//			
//		}

//		}

		if (request.getIsPaidTopupInLastXDays() != null) {
			IsPaidTopupInLastXDays isPaidTopupInLastXDays = request.getIsPaidTopupInLastXDays();
			log.info("Duration:" + isPaidTopupInLastXDays.getDuration());

			String sql = "SELECT * FROM F_TBL_TOPUP"; // " WHERE xxxx="+ isPaidTopupInLastXDays.getDuration();
			log.info(sql);
			result.put("IsPaidTopupInLastXDays", sql);
		}

		if (request.getGetAccumulatedTopups() != null) {
			GetAccumulatedTopups getAccumulatedTopups = request.getGetAccumulatedTopups();
			log.info("Start Day:" + getAccumulatedTopups.getGetAccumulatedTopupsStartDay());
			log.info("Duration:" + getAccumulatedTopups.getGetAccumulatedTopupsDuration());
			log.info("Accumulate Combo:" + getAccumulatedTopups.getVs2__combobox());
			log.info("Accumulate Value:" + getAccumulatedTopups.getGetAccumulatedValue());

			String sql = "SELECT * FROM F_TBL_TOPUP"; // " WHERE xxxx="+ isPaidTopupInLastXDays.getDuration();
			log.info(sql);
			result.put("GetAccumulatedTopups", sql);
		}

		if (result.isEmpty()) {
			result.put("status", false);
		} else {
			result.put("status", true);
		}

		return new ResponseEntity<>(result, HttpStatus.OK);

	}

//	SQL Data New (Saket)
	@RequestMapping(value = { "/generateSQLQuery" }, method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<Map<String, Object>> generateSQLQuery(@RequestBody AdvFltrTblDTO request) {
		Map<String, Object> result = new HashMap<>();

		if (request.getTableName() != null) {
			String tableName = request.getTableName();
			request.getColumnName();

			String queryText = "SELECT MSISDN FROM " + tableName + " where " + request.getColumnName() + " LIKE " + '%'
					+ request.getValue() + '%' + request.getCondition();
			System.out.println(queryText);
			System.out.println("Expected Result : SELECT MSISDN FROM TOPUP_IDD WHERE PRODUCT_NAME LIKE '%TOPUP30%' AND\r\n"
					+ "		 * CREATEDATE < '15/11/2021';");
			if (!OperationUtil.isEmpty(queryText) && queryText != null) {
				result.put("GetPackageFrequency", queryText);
			}
		}
		/*
		 * 
		 * --- Response
		 * 
		 * {
		 * 
		 * "queryText" :
		 * "SELECT MSISDN FROM TOPUP_IDD WHERE PRODUCT_NAME LIKE '%TOPUP30%' AND CREATEDATE < '15/11/2021'"
		 * ,
		 * 
		 * "recordCount" : "12"
		 * 
		 * }
		 * 
		 * 
		 * 
		 * SELECT MSISDN FROM TOPUP_IDD WHERE PRODUCT_NAME LIKE '%TOPUP30%' AND
		 * CREATEDATE < '15/11/2021';
		 * 
		 * 
		 * 
		 * SELECT count(*) FROM TOPUP_IDD WHERE PRODUCT_NAME LIKE '%TOPUP30%' AND
		 * CREATEDATE < '15/11/2021'; -- execute and send the count in recordCount Tag
		 */
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

//	Saket(PREPAID_CX_OFFER_ADVANCE_FILTER)

	@RequestMapping(value = { "/doInsertCXOffer" }, method = { RequestMethod.POST })
	public HttpJsonResult<Hashtable<String, Object>> doInsertCXOffer(
			@RequestBody PrepaidCxOfferAdvanceFilter prepaidCxOfferAdvanceFilter) {
		Hashtable<String, Object> returnTable = new Hashtable<String, Object>();
		HttpJsonResult<Hashtable<String, Object>> result = new HttpJsonResult<Hashtable<String, Object>>(returnTable);
		try {
			if (prepaidCxOfferAdvanceFilter != null) {
				prepaidCxOfferAdvanceFilterService.save(prepaidCxOfferAdvanceFilter);
			}

		} catch (Exception e) {
			log.error("Error", e);
			log.error("[Prepaid Membership][DataController][doInsertCXOffer] failed!", e);
		}
		return result;
	}

//	 List of PREPAID_CX_OFFER_ADVANCE_FILTER
	@GetMapping(value = "listCXOffer")
	public List<PrepaidCxOfferAdvanceFilter> listCXOffer() {

		return prepaidCxOfferAdvanceFilterService.getAllPrepaidCxOffers();
	}

}
