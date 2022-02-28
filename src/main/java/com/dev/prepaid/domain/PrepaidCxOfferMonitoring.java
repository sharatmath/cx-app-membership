package com.dev.prepaid.domain;

import lombok.*;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
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

    private String creditMethod;
    private String operatorId;

    private String usageServiceType;

    private String usageType;
    private String usageOp;
    private Long usageValue;
    private String usageUnit;


    private String countryCode;

    private String arpuType;
    private String arpuOp;
    private Long arpuValue;
    private String arpuSelectedTopUpCode;

    private Boolean isMonitorDateRange;
    private LocalDateTime periodStartDate;
    private LocalDateTime periodEndDate;

    private Boolean isMonitorSpecificPeriod;
    private Integer periodDays;
    private String period;

    private String topUpType;
    private String topUpCode;
    private String topUpCurBalanceOp;
    private Long topUpCurBalanceValue;
    private String topUpAccBalanceBeforeOp;
    private Long topUpAccBalanceBeforeValue;
    private String topUpOp;
    private String topUpTransactionValue;
    private String topUpDaId;
    private String topUpDaBalanceOp;
    private Long topUpDaBalanceValue;
    private String topUpTempServiceClass;
    private String permanentServiceClass;
    private LocalDateTime daExpiryDate;

    //private  String imei;
    private String whitelistType;
    private  Long daChange;
    private  Long chargedAmount;
    private  String roamingFlag;
    private  String ratePlanId;
    private Integer aggregationPeriodDays;
}