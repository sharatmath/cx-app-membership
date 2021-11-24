/**
 * 
 */
package com.dev.prepaid.model;

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
public class AdvFltrTblDTO {

	private String tableName;
	private String columnName;
	private String condition;
	private String value;

}
