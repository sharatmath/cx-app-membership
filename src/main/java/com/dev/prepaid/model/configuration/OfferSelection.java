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
    @SerializedName("offerBucketType")
    private String offerBucketType;
    @SerializedName("offerBucketId")
    private String offerBucketId;
//    @SerializedName("offerBucketCode")
//    private String offerBucketCode;
    @SerializedName("offerCampaignId")
    private Long offerCampaignId;
    @SerializedName("offerCampaignName")
    private String offerCampaignName;
}
