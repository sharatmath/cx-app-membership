/**
 * 
 */
package com.dev.prepaid.service;

import org.springframework.data.repository.CrudRepository;

import com.dev.prepaid.controller.AdvFilterRecordCount;

/**
 * @author Saket
 *
 * 
 */
public interface AdvFilterRecordCountServices extends CrudRepository<AdvFilterRecordCount, String>{

	void getAdvFilterRecordCount(String IN_SQL_QUERY);

}
