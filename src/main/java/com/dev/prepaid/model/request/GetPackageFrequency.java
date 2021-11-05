package com.dev.prepaid.model.request;
/**
 * @author Saket
 *
 * 
 */
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPackageFrequency {
	
	String getPackageFrequencyPackageName;
	int getPackageFrequencyStartMonth;
	int getPackageFrequencyNumberOfMonth;
	int frequencyStart;
	int frequencyEnd;
	boolean otherPackageAllowed;
}
