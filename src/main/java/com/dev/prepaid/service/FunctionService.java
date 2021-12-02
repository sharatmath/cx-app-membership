/**
 * 
 */
package com.dev.prepaid.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dev.prepaid.model.MetaFunctionAdvanceFilter;

/**
 * @author Saket
 *
 * 
 */
public interface FunctionService {
	List<MetaFunctionAdvanceFilter> getAllFunction();

	void saveFunction(MetaFunctionAdvanceFilter functionAdvanceFilter);

	MetaFunctionAdvanceFilter getAdvanceFilterById(long id);

	void deleteAdvanceFilterById(long id);

	Page<MetaFunctionAdvanceFilter> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

	List<MetaFunctionAdvanceFilter> findByName(String functionName);

}
