/**
 * 
 */
package com.dev.prepaid.model.tableRequest;

import java.util.List;

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

	private String tableName;//	clauseDto
	private String condition;
	private List<ClauseDto> clause;
//	private clauseDto(condition+casesDTO include)
//	casesDTO(new
//	
//	"case": "TEXT",
//    "columnName": "PRODUCT_NAME",
//    "operator": "matches",
//    "value": "TOPUP30")
	
	
	
	
//	private String columnName;
//	private String condition;
//	private String value;
//	private String caseType;
//	private String clause;
//	private String operator;
//	private String cases;
	
//	clauseDto
//	casesDTO

}
