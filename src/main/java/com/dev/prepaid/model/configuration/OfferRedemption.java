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
    @SerializedName("isRedemptionCapOnly") Boolean isRedemptionCapOnly;
    @SerializedName("redemptionCapValue") Long redemptionCapValue;
    @SerializedName("isRedemptionCapAndPeriod") Boolean isRedemptionCapAndPeriod;
    @SerializedName("totalRedemptionPeriodValue") Long totalRedemptionPeriodValue;
    @SerializedName("isFrequencyOnly") Boolean isFrequencyOnly;
    @SerializedName("isFrequencyAndTime") Boolean isFrequencyAndTime;
    @SerializedName("frequencyValue") Long frequencyValue;
    @SerializedName("timePeriodValue") Long timePeriodValue;
    @SerializedName("timePeriodType") String timePeriodType;
    @SerializedName("totalRedemptionPeriodType") String totalRedemptionPeriodType;
    @SerializedName("totalRecurringFrequency") Long totalRecurringFrequency;
    @SerializedName("isRecurringFrequencyAndPeriod") Boolean isRecurringFrequencyAndPeriod;
    @SerializedName("recurringFrequencyValue") Long recurringFrequencyValue;
    @SerializedName("recurringFrequencyPeriodType") String recurringFrequencyPeriodType;
    @SerializedName("recurringFrequencyPeriodValue") Long recurringFrequencyPeriodValue;
    @SerializedName("isRecurringFrequencyEachMonth") Boolean isRecurringFrequencyEachMonth;
    @SerializedName("recurringFrequencyDayOfMonth") Long recurringFrequencyDayOfMonth;
    @SerializedName("redemptionMethod") String redemptionMethod;
    @SerializedName("smsKeyword") String smsKeyword;
    @SerializedName("smsKeywordValidityDays") Integer smsKeywordValidityDays;

    @SerializedName("totalRedemptionPeriodEvery") Long totalRedemptionPeriodEvery;
    @SerializedName("timePeriodEvery") Long timePeriodEvery;

// UI Version 4
    @SerializedName("isRecurringFrequency") private boolean isRecurringFrequency;
    @SerializedName("smsCampaignName") private String smsCampaignName;
    @SerializedName("postSmsCampaignName") private String postSmsCampaignName;
    @SerializedName("dynamicVariable1") private String dynamicVariable1;
    @SerializedName("dynamicVariable2") private String dynamicVariable2;
    @SerializedName("dynamicVariable3") private String dynamicVariable3;
    @SerializedName("dynamicVariable4") private String dynamicVariable4;
    @SerializedName("dynamicVariable5") private String dynamicVariable5;
}
