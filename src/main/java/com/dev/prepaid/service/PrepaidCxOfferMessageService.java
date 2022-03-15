/**
 * 
 */
package com.dev.prepaid.service;

import java.util.List;

import com.dev.prepaid.domain.PrepaidCxOfferMessage;

/**
 * @author Saket
 *
 */
public interface PrepaidCxOfferMessageService {
	
	List<PrepaidCxOfferMessage> getCxOfferMessageList(String instanceId);
	public PrepaidCxOfferMessage listCXOffer(String instanceId);
    PrepaidCxOfferMessage saveCxOfferMessage(PrepaidCxOfferMessage prepaidCxOfferMessage);
	PrepaidCxOfferMessage findByCxOfferMessageId(String instanceId);
	
	

}
