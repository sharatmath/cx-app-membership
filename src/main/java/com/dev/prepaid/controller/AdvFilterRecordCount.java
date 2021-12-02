/**
 * 
 */
package com.dev.prepaid.controller;

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
public class AdvFilterRecordCount {
	
	
	private String IN_SQL_QUERY;
	
	private int recordCount;
	
	
	
	


}
