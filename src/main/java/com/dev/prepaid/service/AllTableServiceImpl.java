/**
 * 
 */
package com.dev.prepaid.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.prepaid.domain.AllTables;
import com.dev.prepaid.repository.AllTableRepository;

/**
 * @author Saket
 *
 * 
 */
@Service
public class AllTableServiceImpl implements AllTableService {

	@Autowired
	AllTableRepository allTableRepository;

	@Override
	public List<AllTables> getAllTables() {

		return allTableRepository.findAll();
	}

	@Override
	public void saveTables(AllTables allTables) {

		this.allTableRepository.save(allTables);

	}

	@Override
	public AllTables getTableById(long id) {
		Optional<AllTables> optional = allTableRepository.findById(id);
		AllTables allTables = null;
		if (optional.isPresent()) {
			allTables = optional.get();
		} else {
			throw new RuntimeException(" Table not found for id :: " + id);
		}
		return allTables;
	}

	@Override
	public List<AllTables> findTableByName(String tableName) {
//		List<AllTables> tableList = allTableRepository.findByName(tableName);
//		List<AllTables> allTables = null;
//		if (!tableList.isEmpty() && tableList != null) {
//			allTables = tableList;
//		} else {
//			throw new RuntimeException(" Table not found for id :: " + tableName);
//		}
		return null;
	}

}
