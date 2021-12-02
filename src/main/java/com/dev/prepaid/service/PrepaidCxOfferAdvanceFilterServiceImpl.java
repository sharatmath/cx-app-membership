/**
 * 
 */
package com.dev.prepaid.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.prepaid.domain.PrepaidCxOfferAdvanceFilter;
import com.dev.prepaid.model.MetaFunctionAdvanceFilter;
import com.dev.prepaid.repository.PrepaidCxOfferAdvanceFilterRepository;

/**
 * @author Saket
 *
 * 
 */

@Service
public class PrepaidCxOfferAdvanceFilterServiceImpl implements IPrepaidCxOfferAdvanceFilterService{
	
	@Autowired
	PrepaidCxOfferAdvanceFilterRepository prepaidCxOfferAdvanceFilterRepository;

	@Override
	public List<PrepaidCxOfferAdvanceFilter> getAllPrepaidCxOffers() {
		return (List<PrepaidCxOfferAdvanceFilter>) prepaidCxOfferAdvanceFilterRepository.findAll();
	}

	
//	@Override
//	public PrepaidCxOfferAdvanceFilter findPrepaidCxOfferById(long id) {
//		Optional<PrepaidCxOfferAdvanceFilter> optional = prepaidCxOfferAdvanceFilterRepository.findById(id);
//		PrepaidCxOfferAdvanceFilter prepaidCxOfferAdvanceFilter = null;
//		if (optional.isPresent()) {
//			prepaidCxOfferAdvanceFilter = optional.get();
//		} else {
//			throw new RuntimeException(" Function not found for id :: " + id);
//		}
//		return prepaidCxOfferAdvanceFilter;
//	}
	
	
	
	
	
	

	@Override
	public PrepaidCxOfferAdvanceFilter save(PrepaidCxOfferAdvanceFilter prepaidCxOfferAdvanceFilter) {
		
		
		return this.prepaidCxOfferAdvanceFilterRepository.save(prepaidCxOfferAdvanceFilter);
	}

	@Override
	public void deleteById(int id) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Optional<PrepaidCxOfferAdvanceFilter> findPrepaidCxOfferById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
