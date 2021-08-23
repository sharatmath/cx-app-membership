package com.dev.prepaid.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "PREPAID_CX_OFFER_ELIGIBILITY")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCxOfferEligibility extends Auditable {
    @Id
    @Column(name = "OFFER_ELIGIBILITY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String offerConfigId;
    private Boolean isFrequencyOnly;
    private Boolean isFrequencyAndTime;
    private Long frequency;
    private Integer numberOfFrequency;
    private Integer numberOfDays;
    private Boolean isOfferLevelCapOnly;
    private Boolean isOfferLevelCapAndPeriod;
    private Long offerLevelCapValue;
    private Long offerLevelCapPeriodValue;
    private Long offerLevelCapPeriodDays;
}