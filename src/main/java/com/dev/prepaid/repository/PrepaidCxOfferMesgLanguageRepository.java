/**
 * 
 */
package com.dev.prepaid.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dev.prepaid.domain.PrepaidCxOfferMesgLanguage;

/**
 * @author Saket
 *
 */
public interface PrepaidCxOfferMesgLanguageRepository extends CrudRepository<PrepaidCxOfferMesgLanguage, Long> {

	Optional<PrepaidCxOfferMesgLanguage> findByInstanceId(@Param("instanceId") String instanceId);

}
