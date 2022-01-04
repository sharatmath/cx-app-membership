/**
 * 
 */
package com.dev.prepaid.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.prepaid.domain.PrepaidCxOfferAdvanceFilter;
import com.dev.prepaid.domain.PrepaidCxOfferConfig;
import com.dev.prepaid.repository.PrepaidCxOfferAdvanceFilterRepository;
import com.dev.prepaid.repository.PrepaidCxOfferConfigRepository;

/**
 * @author Saket
 *
 * 
 */

@Service
public class PrepaidCxOfferAdvanceFilterServiceImpl implements IPrepaidCxOfferAdvanceFilterService {

	@Autowired
	PrepaidCxOfferAdvanceFilterRepository prepaidCxOfferAdvanceFilterRepository;

	@Autowired
	PrepaidCxOfferConfigRepository prepaidCxOfferConfigRepository;

	@Override
	public List<PrepaidCxOfferAdvanceFilter> getAllPrepaidCxOffers(String instanceId) {
		Optional<PrepaidCxOfferConfig> opsFindConfig = prepaidCxOfferConfigRepository.findByInstanceId(instanceId);
		List<PrepaidCxOfferAdvanceFilter> arrayList = new ArrayList<>();
		if (opsFindConfig.isPresent()) {
			Optional<PrepaidCxOfferAdvanceFilter> advFilter = prepaidCxOfferAdvanceFilterRepository
					.findByOfferConfigId(opsFindConfig.get().getId());
			if (advFilter.isPresent()) {
				arrayList.add(advFilter.get());
			}

			return arrayList;
		}
		return arrayList;
	}

	@Override
	public List<PrepaidCxOfferAdvanceFilter> listCXOffer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<PrepaidCxOfferAdvanceFilter> findPrepaidCxOfferById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrepaidCxOfferAdvanceFilter save(PrepaidCxOfferAdvanceFilter prepaidCxOfferAdvanceFilter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(int id) {
		// TODO Auto-generated method stub

	}

}
