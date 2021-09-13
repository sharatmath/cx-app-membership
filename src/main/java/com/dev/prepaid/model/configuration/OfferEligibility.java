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
public class OfferEligibility {
    @SerializedName("isFrequencyOnly")
    private Boolean isFrequencyOnly;
    @SerializedName("isFrequencyAndTime")
    private Boolean isFrequencyAndTime;
    @SerializedName("frequency")
    private Long frequency;
    @SerializedName("numberOfFrequency")
    private Integer numberOfFrequency;
    @SerializedName("numberOfDays")
    private Integer numberOfDays;
    @SerializedName("isOfferLevelCapOnly")
    private Boolean isOfferLevelCapOnly;
    @SerializedName("isOfferLevelCapAndPeriod")
    private Boolean isOfferLevelCapAndPeriod;
    @SerializedName("offerLevelCapValue")
    private Long offerLevelCapValue;
    @SerializedName("offerLevelCapPeriodValue")
    private Long offerLevelCapPeriodValue;
    @SerializedName("offerLevelCapPeriodDays")
    private Long offerLevelCapPeriodDays;

    @SerializedName("excludeProgramId")
    private String excludeProgramId;
}
