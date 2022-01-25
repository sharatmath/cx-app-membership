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
public class DataList {

	private String id;

	private String selectedTable;

	private String selectedColumnName;

	private SelectedColumn selectedColumn;

	private String selectedDataType;

	private String selectedOption;

	private String selectedOperand;

	private String selectedValue;

	private String selectedDateType;

	private String exactDate;

	private Integer daysBefore;

	private Integer duration;

	private String numberValue;
}
