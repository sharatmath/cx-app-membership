package com.dev.prepaid.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PREPAID_OFFER_MEMBERSHIP_EXCLUS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidOfferMembershipExclus extends Auditable{
    @Id
    @Column(name = "OFFER_MEMBERSHIP_EXCL_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String invocationId;
    private Long offerEligibilityTxId;
    private Long msisdn;
    private Date offerDate;
    private Boolean isControlFlag;
    private Boolean isGcgFlag;
    private String holdoutGroupId;
    private String evaluationType;
    private String evaluationStatus;
}