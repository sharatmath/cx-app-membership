/**
 * 
 */
package com.dev.prepaid.model.tableRequest;

import java.util.Date;

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

	private String aggregrationOp;

	private String aggregationDateType;

	private String startDate;

	private String dateSeek;

	private int duration;
	
	private String productPackageId;
	
	
//	///New Objs
	
	private int groupId;
	
	private boolean rootGroup;
	
	private String groupCondition;
	
	private String groups;
	
	private String dataId;
	
	private String selectedTable; // String/int??
	
	private String selectedColumnName;
	
	private String selectedDataType;
	
	private String selectedOption;
	
	private String isGreaterThan;//>
	
	private int numberValue;
	
	private String selectedDateType;
	
	private Date exactDates;//exactDate
	
//	private int daysBefore;//daysBefore
	
//	private int duration;//already created
	
	
	
	
	
	

}
