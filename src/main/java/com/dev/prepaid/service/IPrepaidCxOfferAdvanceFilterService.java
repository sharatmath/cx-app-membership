/**
 * 
 */
package com.dev.prepaid.service;

import java.util.List;
import java.util.Optional;

import com.dev.prepaid.domain.PrepaidCxOfferAdvanceFilter;

/**
 * @author Saket
 *
 * 
 */
public interface IPrepaidCxOfferAdvanceFilterService {
	
	List<PrepaidCxOfferAdvanceFilter> getAllPrepaidCxOffers(String instanceId);
    Optional<PrepaidCxOfferAdvanceFilter> findPrepaidCxOfferById(long id);
    PrepaidCxOfferAdvanceFilter save(PrepaidCxOfferAdvanceFilter prepaidCxOfferAdvanceFilter);
    void deleteById(int id);
	List<PrepaidCxOfferAdvanceFilter> listCXOffer();

}
