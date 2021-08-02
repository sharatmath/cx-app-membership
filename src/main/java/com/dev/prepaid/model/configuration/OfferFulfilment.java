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
    @SerializedName("eventType") private String eventType;

    @SerializedName("topupCreditMethod") private String topUpCreditMethod;
    @SerializedName("topupProductPackage") private String topUpProductPackage;
    @SerializedName("topupUsageServiceType") private String topUpUsageServiceType;
    @SerializedName("topupValueOperator") private String topUpValueOperator;
    @SerializedName("topupOperators") private String topUpOperator;
    @SerializedName("topupValue") private String topUpTransactionValue;
    @SerializedName("topupCode") private String topUpCode;

    @SerializedName("arpuCreditMethod") private String arpuCreditMethod;
    @SerializedName("arpuProductPackage") private String arpuProductPackage;
    @SerializedName("arpuUsageServiceType") private String arpuUsageServiceType;
    @SerializedName("arpuOperatorsValue") private String arpuOperatorsValue;
    @SerializedName("arpuTopUpCode") private String arpuTopUpCode;
    @SerializedName("arpuOperators") private String arpuOperators;
    @SerializedName("arpu") private String arpu;
    @SerializedName("arpuPaidOperators") private String arpuPaidOperators;
    @SerializedName("arpuPaidTopUp") private String arpuPaidTopUp;

    @SerializedName("usageServiceType") private String usageServiceType;
    @SerializedName("usageType") private String usageType;
    @SerializedName("usageOperator") private String usageOperator;
    @SerializedName("usageValue") private String usageValue;

    @SerializedName("monitorSpecifiedPeriodRadio") private Boolean monitorSpecifiedPeriodRadio;
    @SerializedName("monitorStartDate") private Date monitorStartDate;
    @SerializedName("monitorEndDate") private Date monitorEndDate;
    @SerializedName("monitorPeriodRadio") private Boolean monitorPeriodRadio;
    @SerializedName("monitorPeriod") private Integer monitorPeriod;
    @SerializedName("monitorPeriodDayMonth") private String monitorPeriodDayMonth;

}
