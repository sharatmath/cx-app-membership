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
import com.dev.prepaid.domain.PrepaidCxOfferMessage;
import com.dev.prepaid.repository.PrepaidCxOfferConfigRepository;
import com.dev.prepaid.repository.PrepaidCxOfferMessageRepository;

/**
 * @author Saket
 *
 */

@Service
public class PrepaidCxOfferMessageServiceImpl implements PrepaidCxOfferMessageService {

	@Autowired
	PrepaidCxOfferMessageRepository cxOfferMessageRepository;

	@Autowired
	PrepaidCxOfferConfigRepository prepaidCxOfferConfigRepository;

	@Override
	public List<PrepaidCxOfferMessage> getCxOfferMessageList(String instanceId) {
		Optional<PrepaidCxOfferConfig> findCXConfig = prepaidCxOfferConfigRepository.findByInstanceId(instanceId);
		List<PrepaidCxOfferMessage> arrayList = new ArrayList<>();
		if (findCXConfig.isPresent()) {
			Optional<PrepaidCxOfferMessage> offerMsg = cxOfferMessageRepository
					.findByInstanceId(findCXConfig.get().getInstanceId());
			if (offerMsg.isPresent()) {
				arrayList.add(offerMsg.get());
			}

			return arrayList;
		}

		return arrayList;
	}

	@Override
	public PrepaidCxOfferMessage listCXOffer(String instanceId) {
		Optional<PrepaidCxOfferConfig> findCXConfig = prepaidCxOfferConfigRepository.findByInstanceId(instanceId);
		if (findCXConfig.isPresent()) {
			Optional<PrepaidCxOfferMessage> offerMsg = cxOfferMessageRepository.findByInstanceId(instanceId);
			if (offerMsg.isPresent())
				return offerMsg.get();
		}
		return new PrepaidCxOfferMessage();
	}

	@Override
	public PrepaidCxOfferMessage saveCxOfferMessage(PrepaidCxOfferMessage prepaidCxOfferAdvanceFilter) {
		return this.cxOfferMessageRepository.save(prepaidCxOfferAdvanceFilter);
	}

	@Override
	public PrepaidCxOfferMessage findByCxOfferMessageId(String instanceId) {
		Optional<PrepaidCxOfferConfig> findCXConfig = prepaidCxOfferConfigRepository.findByInstanceId(instanceId);
		if (findCXConfig.isPresent()) {
			Optional<PrepaidCxOfferMessage> offerMsg = cxOfferMessageRepository
					.findByInstanceId(findCXConfig.get().getInstanceId());
			if (offerMsg.isPresent())
				return offerMsg.get();
		}

		return new PrepaidCxOfferMessage();
	}

}
