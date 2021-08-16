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
public class OfferRedemption {
    @SerializedName("isRedemptionCapOnly")
    private Boolean isRedemptionCapOnly;
    @SerializedName("redemptionCapValue")
    private Long redemptionCapValue;
    @SerializedName("isRedemptionCapAndPeriod")
    private Boolean isRedemptionCapAndPeriod;
    @SerializedName("totalRedemptionPeriodValue")
    private Long totalRedemptionPeriodValue;
    @SerializedName("isFrequencyOnly")
    private Boolean isFrequencyOnly;
    @SerializedName("isFrequencyAndTime")
    private Boolean isFrequencyAndTime;
    @SerializedName("frequencyValue")
    private Long frequencyValue;
    @SerializedName("timePeriodValue")
    private Long timePeriodValue;
    @SerializedName("timePeriodType")
    private String timePeriodType;
    @SerializedName("totalRedemptionPeriodType")
    private String totalRedemptionPeriodType;
    @SerializedName("totalRecurringFrequency")
    private Long totalRecurringFrequency;
    @SerializedName("isRecurringFrequencyAndPeriod")
    private Boolean isRecurringFrequencyAndPeriod;
    @SerializedName("recurringFrequencyValue")
    private Long recurringFrequencyValue;
    @SerializedName("recurringFrequencyPeriodType")
    private String recurringFrequencyPeriodType;
    @SerializedName("recurringFrequencyPeriodValue")
    private Long recurringFrequencyPeriodValue;
    @SerializedName("isRecurringFrequencyEachMonth")
    private Boolean isRecurringFrequencyEachMonth;
    @SerializedName("recurringFrequencyDayOfMonth")
    private Long recurringFrequencyDayOfMonth;
    @SerializedName("redemptionMethod")
    private String redemptionMethod;
    @SerializedName("smsKeyword")
    private String smsKeyword;
    @SerializedName("smsKeywordValidityDays")
    private Integer smsKeywordValidityDays;

    @SerializedName("totalRedemptionPeriodEvery")
    private Integer totalRedemptionPeriodEvery;
    @SerializedName("timePeriodEvery")
    private Integer timePeriodEvery;

}
