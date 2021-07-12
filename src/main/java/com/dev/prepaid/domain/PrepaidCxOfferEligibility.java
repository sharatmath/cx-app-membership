package com.dev.prepaid.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "PREPAID_CX_OFFER_ELIGIBILITY")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCxOfferEligibility {
    @Id
    @Column(name = "OFFER_ELIGIBILITY_ID")
    private Long id;
    private String offerConfigId;
    private Boolean isFrequencyOnly;
    private Boolean isFrequencyAndTime;
    private Long frequency;
    private Integer numberOfDays;
    private Boolean isOfferLevelCapOnly;
    private Boolean isOfferLevelCapAndPeriod;
    private Long offerLevelCapValue;
    private Long offerLevelCapPeriodValue;
}