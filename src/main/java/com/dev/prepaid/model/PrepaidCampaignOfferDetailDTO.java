package com.dev.prepaid.model;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCampaignOfferDetailDTO {
	
	@SerializedName("offerBucketType") private String offerBucketType;
	
	@SerializedName("bucketName") private String bucketName;
	@SerializedName("offerName") private String offerName;
	@SerializedName("offerId") private Long offerId;
	@SerializedName("offerType") private String offerType;
	@SerializedName("description") private String description;
	@SerializedName("value") private double value;
	@SerializedName("valueUnit") private String valueUnit;
	@SerializedName("counterId") private String counterId;
	@SerializedName("counterValue") private Long counterValue;
	@SerializedName("thresholdId") private String thresholdId;
	@SerializedName("thresholdValue") private Long thresholdValue;
	@SerializedName("thresholdValueUnit") private String thresholdValueUnit;
	@SerializedName("valueToDeductFromMa") private Double valueToDeductFromMa;
	@SerializedName("startDate") private String startDate;
	@SerializedName("endDate") private String endDate;
	@SerializedName("action") private String action;
	
	@SerializedName("valueCap") private Long valueCap;
	@SerializedName("valueValidityInDays") private Long valueValidityInDays;


	@SerializedName("offerBucketId") private String offerBucketId;
	@SerializedName("offerCampaignId") private Long offerCampaignId;
	@SerializedName("offerCampaignName") private String offerCampaignName;

	// UI Version 4
//	@SerializedName("smsCampaignName") private String smsCampaignName;
//	@SerializedName("promoCodeList") private String promoCodeList;
//	@SerializedName("messageText1") private String messageText1;
//	@SerializedName("messageText2") private String messageText2;
//	@SerializedName("messageText3") private String messageText3;
//	@SerializedName("messageText4") private String messageText4;
//
//	@SerializedName("overallOfferName") private String overallOfferName;
}
