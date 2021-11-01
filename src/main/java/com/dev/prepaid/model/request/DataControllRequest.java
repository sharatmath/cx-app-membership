package com.dev.prepaid.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * @author Saket
 *
 * 
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataControllRequest {

	GetTopupFrequencyMatchForXMonths getTopupFrequencyMatchForXMonths;
	GetTopupFrequency getTopupFrequency;
	GetPackageFrequency getPackageFrequency;
	HasDonePaidTopupFromTopupcode hasDonePaidTopupFromTopupcode;
	IsFirstPaidTopupofMonth isFirstPaidTopupofMonth;
	HasDoneSinglePaidTopup hasDoneSinglePaidTopup;
	HasDoneSelfServeTopup hasDoneSelfServeTopup;
	
	
	
	
//    'getPackageFrequency',
//    'getTopupFrequency',
//    'getTopupFrequencyMatchForXMonths',
//    'hasDonePaidTopupFromTopupCode',
//    'isFirstPaidTopupofMonth',
//    'hasDoneSinglePaidTopup',
//    'hasDoneSelfServeTopup',
//    'getAccumulatedTopups',
//    'isPaidTopupInLastXDays',
//    'getTopupCountforTopupCode',
//    'getIDDSum',
//    'getIDDBucketPeakDetails',
//    'getIDDBucketPeakStatistic',
//    'packageExclusionCheck',
//    'isDataVasBoughtinLastXDays',
//    'hasDoneinternalSelfServeTopup',
//    'hasDoneTopUpfromOPIDs',
//    'hasBoughtPackageinDuration',
//    'getMonthlyDataPPUSum',
//    'getDAPackageFrequency',
//    'getDABalances',
//    'getDAConsumption',
//    'getDAInitialQuota',
//    'getDACurrentExpiryDate',
//    'getLastRechargeDateofDA',
//    'getLastRechargeDateOfDataTopupID',
//    'getDataVasARPU',
//    'getDailyUsageSum',
//    'isDataUsedinLastXdays',
//    'getAccumulatedMonthlyTopupValue'
}
