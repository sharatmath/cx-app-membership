package com.dev.prepaid.model.configuration;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Config {
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
//	@DateTimeFormat(pattern = "dd-MM-yyyy")
//    private Date startDate;
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
//    @DateTimeFormat(pattern = "dd-MM-yyyy")
//    private Date endDate;
	
	// DROP 1
	@SerializedName("offerSelections")
	private List<OfferSelection> offerSelections;

	@SerializedName("offerEligibility")
	private OfferEligibility offerEligibility;

	@SerializedName("offerMonitoring")
	private OfferMonitoring offerMonitoring;

	@SerializedName("offerRedemption")
	private OfferRedemption offerRedemption;

	@SerializedName("offerFulfilment")
	private OfferFulfilment OfferFulfilment;

	@SerializedName("notification")
    private Boolean notification;

	// DROP 2
//	@SerializedName("audienceFilterCheckbox")
//    private Boolean audienceFilterCheckbox;
//	@SerializedName("audienceFilterId")
//    private String audienceFilterId;
//	@SerializedName("audienceFilterName")
//    private String audienceFilterName;
	
//	@SerializedName("eligibilityCheckbox")
//    private Boolean eligibilityCheckbox;
//	@SerializedName("eligibilityId")
//    private String eligibilityId;
//	@SerializedName("eligibilityName")
//    private String eligibilityName;
//
//	@SerializedName("fulfillmentCheckbox")
//    private Boolean fulfillmentCheckbox;
//	@SerializedName("fulfillmentId")
//    private String fulfillmentId;
//	@SerializedName("fulfillmentName")
//    private String fulfillmentName;
	
//	@SerializedName("redemptionCheckbox")
//    private Boolean redemptionCheckbox;
//	@SerializedName("redemptionId")
//    private String redemptionId;
//	@SerializedName("redemptionName")
//    private String redemptionName;
	
	@SerializedName("uuid")
    private String uuid;
	
	// DA OFFERS
	
	
	// OMS OFFERS
//	@SerializedName("omsOfferNameId")
//	@SerializedName("omsOfferTypeId")
//	@SerializedName("omsValidityId")
//	@SerializedName("omsDeductMaId")
//	@SerializedName("omsThrId")
//	@SerializedName("omsThrValUnitId")
//	@SerializedName("omsEndDateId")
//	@SerializedName("omsOfferIdId")
//	@SerializedName("omsDescId")
//	@SerializedName("omsValidUnitId")
//	@SerializedName("omsCntId")
//	@SerializedName("omsThrValId")
//	@SerializedName("omsStartDateId")
//	@SerializedName("omsActionId")
	
	//DROP 2 1
	@SerializedName("programId")
	private String programId;
	@SerializedName("type")
	private String type;
	@SerializedName("programName")
	private String programName;



}
