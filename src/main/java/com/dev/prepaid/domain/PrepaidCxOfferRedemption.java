package com.dev.prepaid.domain;

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
    private Boolean isFrequencyOnly;
    private Boolean isFrequencyAndTime;
    private Long frequencyValue;
    private Long timePeriodValue;
    private String timePeriodType;
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
}
