package com.dev.prepaid.model.configuration;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * hitory
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Config {
	// DROP 1
	@SerializedName("offerSelection")
	private List<OfferSelection> offerSelection;

	@SerializedName("offerPromoCode")
	private OfferPromoCode offerPromoCode;

	@SerializedName("offerEligibility")
	private OfferEligibility offerEligibility;

	@SerializedName("offerMonitoring")
	private OfferFulfilment offerMonitoring;

	@SerializedName("offerEventCondition")
	private EventCondition offerEventCondition;

	@SerializedName("offerRedemption")
	private OfferRedemption offerRedemption;

	@SerializedName("notification")
	private Boolean notification;

	@SerializedName("uuid")
	private String uuid;

	// DROP 2 1
//	@SerializedName("programId")
//	private String programId;
	@SerializedName("type")
	private String type;
//	@SerializedName("programName")
//	private String programName;

	@SerializedName("advancedFilter")
	private PrepaidCxOfferAdvanceFilter prepaidCxOfferAdvanceFilter;

}
