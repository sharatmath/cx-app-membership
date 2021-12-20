package com.dev.prepaid.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 06-Sep-2021 change model sprint1
 */
@Entity
@Table(name = "PREPAID_OFFER_MEMBERSHIP")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidOfferMembership extends Auditable {
    @Id
    @Column(name = "OFFER_MEMBERSHIP_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String invocationId;
    private Long offerEligibilityTxId;
    private String offerConfigId;
    private Long msisdn;
    private Date offerDate;
    private LocalDateTime monitoringStartDate;
    private LocalDateTime monitoringEndDate;
    private String optinFlag;
    // sprint 1
    private String offerMembershipStatus;

}
