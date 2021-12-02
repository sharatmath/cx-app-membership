/**
 * 
 */
package com.dev.prepaid.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.prepaid.model.MetaFunctionAdvanceFilter;

/**
 * @author Saket
 *
 * 
 */
public interface FunctionRepository extends JpaRepository<MetaFunctionAdvanceFilter, Long>{
	
//	List<MetaFunctionAdvanceFilter> findByName(String functionName);

	


}
