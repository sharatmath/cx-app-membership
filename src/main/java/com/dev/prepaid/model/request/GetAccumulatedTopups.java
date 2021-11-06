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
public class GetAccumulatedTopups {
	int getAccumulatedTopupsDuration;
	String vs2__combobox;
	int getAccumulatedTopupsStartDay;
	int getAccumulatedValue;
	
}
