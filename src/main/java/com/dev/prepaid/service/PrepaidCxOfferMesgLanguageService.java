/**
 * 
 */
package com.dev.prepaid.service;

import java.util.List;

import com.dev.prepaid.domain.PrepaidCxOfferMesgLanguage;

/**
 * @author Saket
 *
 */
public interface PrepaidCxOfferMesgLanguageService {
	
	List<PrepaidCxOfferMesgLanguage> getCxOfferMessageLanguageList(String instanceId);
	public PrepaidCxOfferMesgLanguage listCxOfferMessageLanguage(String instanceId);
    PrepaidCxOfferMesgLanguage saveCxOfferMessageLanguage(PrepaidCxOfferMesgLanguage cxOfferMesgLanguage);
	PrepaidCxOfferMesgLanguage findByCxOfferMessageLanguageId(String instanceId);

}
