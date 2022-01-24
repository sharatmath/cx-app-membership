/**
 * 
 */
package com.dev.prepaid.service;

import java.util.List;

import com.dev.prepaid.domain.AllTables;

/**
 * @author Saket
 *
 * 
 */
public interface AllTableService {
	
	List<AllTables> getAllTables();

	void saveTables(AllTables allTables);

	AllTables getTableById(long id);

	List<AllTables> findTableByName(String tableName);

}
