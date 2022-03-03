package com.dev.prepaid.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "PREPAID_CX_OFFER_EVENT_CONDITION")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCxOfferEventCondition extends Auditable{
    @Id
    @Column(name = "OFFER_EVENT_CONDITION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String offerConfigId;
    private LocalDateTime campaignStartDate;
    private LocalDateTime campaignEndDate;
    private String eventConditionName;
    private String eventConditionType;

    private String creditMethod;
    private String usageServiceType;
    private String operatorId;

    private String topUpType;
    private String topUpCode;
    private String topUpCurBalanceOp;
    private Long topUpCurBalanceValue;
    private String topUpAccBalanceBeforeOp;
    private Long topUpAccBalanceBeforeValue;
    private String topUpOp;
    private String topUpTransactionValue;
    private String daId;
    private String daBalanceOp;
    private Long daBalanceValue;
    private String tempServiceClass;
    private String permanentServiceClass;
    private  String imei;
    private  Long daChange;
    private  Long chargedAmount;
    private  LocalDateTime daExpiryDate;

    private String eventTypeUsages;
    private String eventUsagesOp;
    private Long eventUsagesValue;
    private String eventUsagesUnit;
    private Integer aggregationPeriodDays;

    private String countryCode;

    private String arpuType;
    private String arpuOp;
    private Long arpuValue;
    private String arpuSelectedTopUpCode;

    private  String roamingFlag;
    private  String ratePlanId;
}
