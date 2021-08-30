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
public class EventCondition {
    @SerializedName("campaignStartDate") private Date campaignStartDate;
    @SerializedName("campaignEndDate") private  Date campaignEndDate;
    @SerializedName("eventConditionName") private  String eventConditionName;
    @SerializedName("eventConditionType") private  String eventConditionType;

    @SerializedName("creditMethod") private  String creditMethod;
    @SerializedName("usageServiceType") private  String usageServiceType;
    @SerializedName("operatorId") private  String operatorId;

    @SerializedName("topupCode") private  String topUpCode;


    @SerializedName("topupCurBalanceOp") private  String topUpCurBalanceOp;
    @SerializedName("topupCurBalanceValue") private  Long topUpCurBalanceValue;

    @SerializedName("topupAccBalanceBeforeOp") private  String topUpAccBalanceBeforeOp;
    @SerializedName("topupAccBalanceBeforeValue") private  Long topUpAccBalanceBeforeValue;

    @SerializedName("topupOp") private  String topUpOp;
    @SerializedName("topupTransactionValue") private  Long topUpTransactionValue;

    @SerializedName("DaId") private  String daId;
    @SerializedName("DaBalanceOp") private  String daBalanceOp;
    @SerializedName("DaBalanceValue") private  Long daBalanceValue;
    @SerializedName("TempServiceClass") private  String tempServiceClass;
    @SerializedName("imei") private  String imei;
    @SerializedName("daChange") private  String daChange;
    @SerializedName("chargedAmount") private  String chargedAmount;

    @SerializedName("eventTypeUsages") private  Long eventTypeUsages;
    @SerializedName("eventUsagesOp") private  String eventUsagesOp;
    @SerializedName("eventUsagesValue") private  Long eventUsagesValue;
    @SerializedName("aggregationPeriodDays") private  Integer aggregationPeriodDays;

    @SerializedName("countryCode") private  String countryCode;

    @SerializedName("arpuType") private  String arpuType;
    @SerializedName("arpuOp") private  String arpuOp;
    @SerializedName("arpuValue") private  Long arpuValue;
    @SerializedName("arpuSelectedTopUpCode") private  String arpuSelectedTopUpCode;
}
