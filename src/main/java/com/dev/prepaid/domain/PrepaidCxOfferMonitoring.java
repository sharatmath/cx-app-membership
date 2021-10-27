package com.dev.prepaid.domain;

import com.google.gson.annotations.SerializedName;
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

    private String creditMethod;
    private String operatorId;

    private String usageServiceType;

    private String usageType;
    private String usageOp;
    private Long usageValue;

    private String countryCode;

    private String arpuType;
    private String arpuOp;
    private Long arpuValue;
    private String arpuSelectedTopUpCode;

    private Boolean isMonitorDateRange;
    private Date periodStartDate;
    private Date periodEndDate;

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
    private Long topUpTransactionValue;
    private String topUpDaId;
    private String topUpDaBalanceOp;
    private Long topUpDaBalanceValue;
    private String topUpTempServiceClass;

    private Date daExpiryDate;

    private  String imei;
    private  String daChange;
    private  Long chargedAmount;
    private  String roamingFlag;
    private  String ratePlanId;
    private Integer aggregationPeriodDays;
}