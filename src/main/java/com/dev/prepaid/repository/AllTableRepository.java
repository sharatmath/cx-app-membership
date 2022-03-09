/**
 * 
 */
package com.dev.prepaid.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.prepaid.domain.AllTables;

/**
 * @author Saket
 *
 * 
 */
@Repository
public interface AllTableRepository extends JpaRepository<AllTables, Long>{
	
//	List<AllTables> findByName(String tableName);

}
