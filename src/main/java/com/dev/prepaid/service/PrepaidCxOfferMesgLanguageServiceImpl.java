/**
 * 
 */
package com.dev.prepaid.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.prepaid.domain.PrepaidCxOfferConfig;
import com.dev.prepaid.domain.PrepaidCxOfferMesgLanguage;
import com.dev.prepaid.repository.PrepaidCxOfferConfigRepository;
import com.dev.prepaid.repository.PrepaidCxOfferMesgLanguageRepository;

/**
 * @author Saket
 *
 */

@Service
public class PrepaidCxOfferMesgLanguageServiceImpl implements PrepaidCxOfferMesgLanguageService{
	
	@Autowired
	PrepaidCxOfferMesgLanguageRepository cxOfferMesgLanguageRepository;

	@Autowired
	PrepaidCxOfferConfigRepository prepaidCxOfferConfigRepository;

	
//	get the List
	@Override
	public List<PrepaidCxOfferMesgLanguage> getCxOfferMessageLanguageList(String instanceId) {
		Optional<PrepaidCxOfferConfig> findCXConfig = prepaidCxOfferConfigRepository.findByInstanceId(instanceId);
		List<PrepaidCxOfferMesgLanguage> arrayList = new ArrayList<>();
		if (findCXConfig.isPresent()) {
			Optional<PrepaidCxOfferMesgLanguage> offerMsg = cxOfferMesgLanguageRepository
					.findByInstanceId(findCXConfig.get().getInstanceId());
			if (offerMsg.isPresent()) {
				arrayList.add(offerMsg.get());
			}

			return arrayList;
		}

		return arrayList;
	}
	

	@Override
	public PrepaidCxOfferMesgLanguage listCxOfferMessageLanguage(String instanceId) {
		Optional<PrepaidCxOfferConfig> findCXConfig = prepaidCxOfferConfigRepository.findByInstanceId(instanceId);
		if (findCXConfig.isPresent()) {
			Optional<PrepaidCxOfferMesgLanguage> offerMsg = cxOfferMesgLanguageRepository.findByInstanceId(instanceId);
			if (offerMsg.isPresent())
				return offerMsg.get();
		}
		return new PrepaidCxOfferMesgLanguage();
	}

	@Override
	public PrepaidCxOfferMesgLanguage saveCxOfferMessageLanguage(PrepaidCxOfferMesgLanguage offerMesgLanguage) {
		return this.cxOfferMesgLanguageRepository.save(offerMesgLanguage);
	}

	@Override
	public PrepaidCxOfferMesgLanguage findByCxOfferMessageLanguageId(String instanceId) {
		Optional<PrepaidCxOfferConfig> findCXConfig = prepaidCxOfferConfigRepository.findByInstanceId(instanceId);
		if (findCXConfig.isPresent()) {
			Optional<PrepaidCxOfferMesgLanguage> offerMsg = cxOfferMesgLanguageRepository
					.findByInstanceId(findCXConfig.get().getInstanceId());
			if (offerMsg.isPresent())
				return offerMsg.get();
		}

		return new PrepaidCxOfferMesgLanguage();
	}
	
}
