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

/**
 * 06-Sept-2021 comment provisionDate,provisionStatus Sprint 1
 * 06-Sept-2021 add fulfillmentStatus, fulfillmentDate offerSelectionId Sprint 1
 */
@Entity
@Table(name = "PREPAID_OFFER_MONITORING_TX")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidOfferMonitoringTrx extends Auditable{
    @Id
    @Column(name = "OFFER_MONITORING_TX_ID")
    private Long id;
    private Long offerMembershipId;
    private Long msisdn;
//    private Date provisionDate;
//    private String provisionStatus;

    private String fulfillmentStatus;
    private Date fulfillmentDate;
    private Long offerSelectionId;
}
