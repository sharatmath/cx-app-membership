package com.dev.prepaid.model.configuration;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class EventCondition {
    @SerializedName("campaignStartDate") private String campaignStartDate;
    @SerializedName("campaignEndDate") private  String campaignEndDate;
    @SerializedName("eventConditionName") private  String eventConditionName;
    @SerializedName("eventType") private  String eventType;

    @SerializedName("creditMethod") private  String creditMethod;
    @SerializedName("usageServiceType") private  String usageServiceType;
    @SerializedName("operatorId") private  String operatorId;

    @SerializedName("topupCode") private  String topUpCode;
    @SerializedName("topupType") private  String topUpType;

    @SerializedName("topupCurBalanceOp") private  String topUpCurBalanceOp;
    @SerializedName("topupCurBalanceValue") private  Long topUpCurBalanceValue;

    @SerializedName("topupAccBalanceBeforeOp") private  String topUpAccBalanceBeforeOp;
    @SerializedName("topupAccBalanceBeforeValue") private  Long topUpAccBalanceBeforeValue;

    @SerializedName("topUpDaId") private  String topUpDaId;
    @SerializedName("topUpDaBalanceOp") private  String topUpDaBalanceOp;
    @SerializedName("topUpDaBalanceValue") private  Long topUpDaBalanceValue;
    @SerializedName("topUpTempServiceClass") private  String topUpTempServiceClass;

    @SerializedName("topupOp") private  String topUpOp;
    @SerializedName("topupTransactionValue") private  String topUpTransactionValue;

    @SerializedName("daId") private  String daId;
    @SerializedName("daBalanceOp") private  String daBalanceOp;
    @SerializedName("daBalanceValue") private  Long daBalanceValue;
    @SerializedName("tempServiceClass") private  String tempServiceClass;
    @SerializedName("imei") private  String imei;
    @SerializedName("daChange") private  Long daChange;
    @SerializedName("chargedAmount") private  Long chargedAmount;

    @SerializedName("eventTypeUsages") private  String eventTypeUsages;
    @SerializedName("eventUsagesOp") private  String eventUsagesOp;
    @SerializedName("eventUsagesValue") private  Long eventUsagesValue;
    @SerializedName("eventUsagesUnit") private  String eventUsagesUnit;
    @SerializedName("aggregationPeriodDays") private  Integer aggregationPeriodDays;

    @SerializedName("countryCode") private  String countryCode;

    @SerializedName("arpuType") private  String arpuType;
    @SerializedName("arpuOp") private  String arpuOp;
    @SerializedName("arpuValue") private  Long arpuValue;
    @SerializedName("arpuSelectedTopUpCode") private  String arpuSelectedTopUpCode;

    @SerializedName("roamingFlag") private  String roamingFlag;
    @SerializedName("ratePlanId") private  String ratePlanId;

    @SerializedName("daExpiryDate")  private String daExpiryDate;
    @SerializedName("permanentServiceClass") private  String permanentServiceClass;

}
