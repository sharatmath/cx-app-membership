/**
 * 
 */
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
public class HasDoneSinglePaidTopup {
	
	String getTopupFrequencyMatchForXMonthsPackageName;
	int getPackageFrequencyStartMonth;
	int getPackageFrequencyNumberOfMonth;
	int frequencyStart;
	int frequencyEnd;
	boolean otherPackageAllowed;

}
