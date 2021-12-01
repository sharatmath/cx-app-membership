/**
 * 
 */
package com.dev.prepaid.model.tableRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Saket
 *
 * 
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CasesDTO {
	
	private String columnName;
	private String value;
	private String caseType;
	private String operator;
	private boolean exactDate;
	private boolean daysBefore;
	private boolean daysAfter;
	private int numberOfDays;

}
