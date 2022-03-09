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
	
	List<PrepaidCxOfferAdvanceFilter> getAllPrepaidCxOfferList(String instanceId);
	public PrepaidCxOfferAdvanceFilter listCXOffer(String instanceId);
    Optional<PrepaidCxOfferAdvanceFilter> findPrepaidCxOfferById(long id);
    PrepaidCxOfferAdvanceFilter save(PrepaidCxOfferAdvanceFilter prepaidCxOfferAdvanceFilter);
    void deleteById(int id);
	List<PrepaidCxOfferAdvanceFilter> listCXOffer();
//	Optional<PrepaidCxOfferAdvanceFilter> findByOfferConfigId(@Param("offerConfigId") String offerConfigId);
//	PrepaidCxOfferAdvanceFilter listCXOffer(String instanceId);
	public PrepaidCxOfferAdvanceFilter getCXOfferList(String instanceId);
	
	PrepaidCxOfferAdvanceFilter findByOfferConfigId(String offerConfigId);
}
