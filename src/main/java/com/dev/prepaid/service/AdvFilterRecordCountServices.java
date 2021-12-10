/**
 * 
 */
package com.dev.prepaid.service;

import java.math.BigDecimal;

/**
 * @author Saket
 *
 * 
 */
public interface AdvFilterRecordCountServices {

	BigDecimal getAdvFilterRecordCount(String IN_SQL_QUERY);

	String getAdvFilterNumber(String IN_SQL_NUM_QUERY);

}
