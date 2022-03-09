/**
 * 
 */
package com.dev.prepaid.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dev.prepaid.domain.PrepaidCxOfferMessage;

/**
 * @author Saket
 *
 */
public interface PrepaidCxOfferMessageRepository extends CrudRepository<PrepaidCxOfferMessage, Long> {

//	Optional<PrepaidCxOfferMessage> findByOfferConfigId(@Param("instanceId") String instanceId);

	Optional<PrepaidCxOfferMessage> findByInstanceId(@Param("instanceId") String instanceId);

}
