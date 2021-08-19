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
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
    @SequenceGenerator(name = "seqGen", sequenceName = "OFFER_MEMBERSHIP_EXCL_ID_SEQ", initialValue = 1)
    private Long id;
    private Long offerSelectionId;
    private Long msisdn;
    private Date offerDate;
    private Date topupDate;
    private Boolean isControlFlag;
    private Boolean isGcgFlag;
    private String holdoutGroupId;
}