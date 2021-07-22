package com.dev.prepaid.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PREPAID_OFFER_MEMBERSHIP")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidOfferMembership {
    @Id
    @Column(name = "OFFER_MEMBERSHIP_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long offerSelectionId;
    private String offerConfigId;
    private Long msisdn;
    private Date offerDate;
    private Date redemptionDate;
    private Date topupDate;
    private Long couponCodeId;
    private Long couponCodeStatus;
    private String notificationChannel;
    private String opidTakeupChannel;
    private String notificationMessage;
    private String redeemFlag;
    private Date monitoringStartDate;
    private Date monitoringEndDate;
    private String fulfillmentStatus;
    private Date fulfillmentDate;
    private String optinFlag;
}
