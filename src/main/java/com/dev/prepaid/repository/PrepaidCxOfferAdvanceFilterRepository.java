/**
 * 
 */
package com.dev.prepaid.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dev.prepaid.domain.PrepaidCxOfferAdvanceFilter;

/**
 * @author Saket
 *
 * 
 */
@Repository
public interface PrepaidCxOfferAdvanceFilterRepository extends  CrudRepository<PrepaidCxOfferAdvanceFilter, Long> {

}
