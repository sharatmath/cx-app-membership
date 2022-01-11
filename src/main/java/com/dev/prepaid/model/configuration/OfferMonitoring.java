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
public class OfferMonitoring {
    @SerializedName("eventType") private String eventType;
    @SerializedName("usageServiceType") private  String usageServiceType;
    @SerializedName("productPackage") private  String productPackage;
    @SerializedName("creditMethod") private  String creditMethod;
    @SerializedName("operatorId") private  String operatorId;
    @SerializedName("periodDays") private  Integer periodDays;
    @SerializedName("periodStartDate") private  String periodStartDate;
    @SerializedName("periodEndDate") private  String periodEndDate;
    @SerializedName("topupCode") private  String topupCode;
    @SerializedName("usageType") private  String usageType;
    @SerializedName("transactionValue") private  String transactionValue;
    @SerializedName("operatorValue") private  String operatorValue;
    @SerializedName("paidArpuOperator") private  String paidArpuOperator;
    @SerializedName("paidArpuValue") private  Long paidArpuValue;
    @SerializedName("isMonitorDateRange") private  Boolean isMonitorDateRange;
    @SerializedName("isMonitorSpecificPeriod") private  Boolean isMonitorSpecificPeriod;
    @SerializedName("imei") private  String imei;
    @SerializedName("daChange") private  String daChange;
    @SerializedName("chargedAmount") private  String chargedAmount;
}
