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
@Table(name = "PREPAID_OFFER_MONITORING_TX")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidOfferMonitoringTrx {
    @Id
    @Column(name = "OFFER_MONITORING_ID")
    private Long id;
    private Long offerMembershipId;
    private Long msisdn;
    private Date provisionDate;
    private String provisionStatus;
}
