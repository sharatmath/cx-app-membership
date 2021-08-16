package com.dev.prepaid.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PREPAID_CX_OFFER_MONITORING")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCxOfferMonitoring extends Auditable {
    @Id
    @Column(name = "OFFER_MONITORING_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String offerConfigId;
    private String eventType;
    private String eventCampaignName;
    private Date eventStartDate;
    private Date eventEndDate;
    private String usageServiceType;
    private String productPackage;
    private String creditMethod;
    private String operatorId;
    private String period;
    private Integer periodDays;
    private Date periodStartDate;
    private Date periodEndDate;
    private String topupCode;
    private String usageType;
    private Long transactionValue;
    private String operatorValue;
    private String paidArpuOperator;
    private Long paidArpuValue;
    private Boolean isMonitorDateRange;
    private Boolean isMonitorSpecificPeriod;
}