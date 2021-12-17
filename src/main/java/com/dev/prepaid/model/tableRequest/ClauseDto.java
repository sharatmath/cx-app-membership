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
public class ClauseDto {
	
	private List<CasesDTO> cases;

}
