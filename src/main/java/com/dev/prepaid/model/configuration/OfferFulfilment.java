package com.dev.prepaid.model.configuration;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferFulfilment {
    @SerializedName("offerMonitoringType") private String eventType;

    @SerializedName("topupCreditMethod") private String topUpCreditMethod;
    @SerializedName("topupUsageServiceType") private String topUpUsageServiceType;
    @SerializedName("topupOperators") private String topUpOperator;
    @SerializedName("topupCode") private String topUpCode;
    @SerializedName("topupType") private String topUpType;


    @SerializedName("topupCurBalanceOp") private  String topUpCurBalanceOp;
    @SerializedName("topupCurBalanceValue") private  Long topUpCurBalanceValue;
    @SerializedName("topupAccBalanceBeforeOp") private  String topUpAccBalanceBeforeOp;
    @SerializedName("topupAccBalanceBeforeValue") private  Long topUpAccBalanceBeforeValue;
    @SerializedName("topupOp") private  String topUpOp;
    @SerializedName("topupTransactionValue") private  Long topUpTransactionValue;
    @SerializedName("topupDaId") private  String topUpDaId;
    @SerializedName("topupDaBalanceOp") private  String topUpDaBalanceOp;
    @SerializedName("topupDaBalanceValue") private  Long topUpDaBalanceValue;
    @SerializedName("topupTempServiceClass") private  String topUpTempServiceClass;

    @SerializedName("operatorId") private String operatorId;
    @SerializedName("arpuType") private String arpuType;
    @SerializedName("arpuSelectedTopUpCode") private String arpuSelectedTopUpCode;
    @SerializedName("arpuOp") private String arpuOp;
    @SerializedName("arpuValue") private Long arpuValue;

    @SerializedName("usageServiceType") private String usageServiceType;
    @SerializedName("usageType") private String usageType;
    @SerializedName("usageOperator") private String usageOperator;
    @SerializedName("usageValue") private Long usageValue;

    @SerializedName("countryCode") private String countryCode;

    @SerializedName("monitorSpecifiedPeriodRadio") private Boolean monitorSpecifiedPeriodRadio;
    @SerializedName("monitorStartDate") private String monitorStartDate;
    @SerializedName("monitorEndDate") private String monitorEndDate;
    @SerializedName("monitorPeriodRadio") private Boolean monitorPeriodRadio;
    @SerializedName("monitorPeriod") private Integer monitorPeriod;
    @SerializedName("monitorPeriodDayMonth") private String monitorPeriodDayMonth;

    @SerializedName("imei") private  String imei;
    @SerializedName("daChange") private  String daChange;
    @SerializedName("chargedAmount") private  String chargedAmount;
    @SerializedName("roamingFlag") private  String roamingFlag;
    @SerializedName("ratePlanId") private  String ratePlanId;
    @SerializedName("aggregationPeriodDays")  private Integer aggregationPeriodDays;

    @SerializedName("daExpiryDate")  private Date daExpiryDate;

}
