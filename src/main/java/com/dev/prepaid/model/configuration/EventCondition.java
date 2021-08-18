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
    @SerializedName("campaignStartDate")
    private Date campaignStartDate;
    @SerializedName("campaignEndDate")
    private Date campaignEndDate;
    @SerializedName("eventConditionName")
    private String eventConditionName;
    @SerializedName("eventConditionType")
    private String eventConditionType;

    @SerializedName("creditMethod")
    private String creditMethod;
    @SerializedName("usageServiceType")
    private String usageServiceType;
    @SerializedName("operatorId")
    private String operatorId;

    @SerializedName("topupCode")
    private String topupCode;


    @SerializedName("topupCurBalanceOp")
    private String topupCurBalanceOp;
    @SerializedName("topupCurBalanceValue")
    private Long topupCurBalanceValue;

    @SerializedName("topupAccBalanceBeforeOp")
    private String topupAccBalanceBeforeOp;
    @SerializedName("topupAccBalanceBeforeValue")
    private Long topupAccBalanceBeforeValue;

    @SerializedName("topupOp")
    private String topupOp;
    @SerializedName("topupTransactionValue")
    private Long topupTransactionValue;

    @SerializedName("topupDaId")
    private String topupDaId;
    @SerializedName("topupDaBalanceOp")
    private String topupDaBalanceOp;
    @SerializedName("topupDaBalanceValue")
    private Long topupDaBalanceValue;
    @SerializedName("topupTempServiceClass")
    private String topupTempServiceClass;

    @SerializedName("eventTypeUsages")
    private Long eventTypeUsages;
    @SerializedName("eventUsagesOp")
    private String eventUsagesOp;
    @SerializedName("eventUsagesValue")
    private Long eventUsagesValue;
    @SerializedName("aggregationPeriodDays")
    private Integer aggregationPeriodDays;

    @SerializedName("countryCode")
    private String countryCode;

    @SerializedName("arpuType")
    private String arpuType;
    @SerializedName("arpuOp")
    private String arpuOp;
    @SerializedName("arpuValue")
    private Long arpuValue;
    @SerializedName("arpuSelectedTopUpCode")
    private String arpuSelectedTopUpCode;
}
