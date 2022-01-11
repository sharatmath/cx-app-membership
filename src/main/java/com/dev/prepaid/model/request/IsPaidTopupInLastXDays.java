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
public class IsPaidTopupInLastXDays {
	int duration;
}
