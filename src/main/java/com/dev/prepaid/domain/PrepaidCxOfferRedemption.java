package com.dev.prepaid.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PREPAID_CX_OFFER_REDEMPTION")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCxOfferRedemption extends Auditable{
    @Id
    @Column(name = "OFFER_REDEMPTION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String offerConfigId;
    private Boolean isRedemptionCapOnly;
    private Long redemptionCapValue;
    private Boolean isRedemptionCapAndPeriod;
    private Long totalRedemptionPeriodValue;
    private Long totalRedemptionPeriodEvery;
    private Boolean isFrequencyOnly;
    private Boolean isFrequencyAndTime;
    private Long frequencyValue;
    private Long timePeriodValue;
    private String timePeriodType;
    private Long timePeriodEvery;
    private String totalRedemptionPeriodType;
    private Long totalRecurringFrequency;
    private Boolean isRecurringFrequencyAndPeriod;
    private Long recurringFrequencyValue;
    private String recurringFrequencyPeriodType;
    private Long recurringFrequencyPeriodValue;
    private Boolean isRecurringFrequencyEachMonth;
    private Long recurringFrequencyDayOfMonth;
    private String redemptionMethod;
    private String smsKeyword;
    private Integer smsKeywordValidityDays;
 // UI Version4
    private boolean isRecurringProvisioning;
    private String smsCampaignName;
    private String postSmsCampaignName;
    private String dynamicVariable1;
    private String dynamicVariable2;
    private String dynamicVariable3;
    private String dynamicVariable4;
    private String dynamicVariable5;

    private String optKeyword;
    private boolean isDateRange;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date optStartDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date optEndDate;
    private boolean isPeriod;
    private Integer optPeriod;




}
