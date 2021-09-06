package com.dev.prepaid.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 06-Sep-2021 change model     sprint1
 */
@Entity
@Table(name = "PREPAID_OFFER_MEMBERSHIP")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidOfferMembership extends Auditable{
    @Id
    @Column(name = "OFFER_MEMBERSHIP_ID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
    @SequenceGenerator(name = "seqGen", sequenceName = "OFFER_MEMBERSHIP_ID_SEQ", initialValue = 1)
    private Long id;
//    private Long offerSelectionId;
    private String offerConfigId;
    private Long msisdn;
    private Date offerDate;
    private Date redemptionDate;
//    private Date topupDate;
//    private Long couponCodeId;
//    private Long couponCodeStatus;
//    private String notificationChannel;
//    private String opidTakeupChannel;
//    private String notificationMessage;
//    private String redeemFlag;
    private Date monitoringStartDate;
    private Date monitoringEndDate;
//    private String fulfillmentStatus;
//    private Date fulfillmentDate;
    private String optinFlag;
// sprint 1
private String offerMembershipStatus;

}
