package com.dev.prepaid.model.configuration;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferSelection {
    @SerializedName("offerBucketType") private String offerBucketType;
    @SerializedName("offerBucketId") private String offerBucketId;
//    @SerializedName("offerBucketCode") private String offerBucketCode;
    @SerializedName("offerCampaignId") private Long offerCampaignId;
    @SerializedName("offerCampaignName") private String offerCampaignName;

//UI Version 4
    @SerializedName("smsCampaignName") private String smsCampaignName;
    @SerializedName("offerType") private String offerType;
    @SerializedName("promoCodeList") private String promoCodeList;
    @SerializedName("messageText1") private String messageText1;
    @SerializedName("messageText2") private String messageText2;
    @SerializedName("messageText3") private String messageText3;
    @SerializedName("messageText4") private String messageText4;



}
