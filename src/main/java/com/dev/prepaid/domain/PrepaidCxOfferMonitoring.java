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
@Table(name = "PREPAID_CX_OFFER_MONITORING")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCxOfferMonitoring {
    @Id
    @Column(name = "OFFER_MONITORING_ID")
    private Long id;
    private String offerConfigId;
    private String eventType;
    private String usageServiceType;
    private String productPackage;
    private String creditMethod;
    private String operatorId;
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