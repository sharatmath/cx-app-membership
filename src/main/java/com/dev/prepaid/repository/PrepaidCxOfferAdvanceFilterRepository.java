package com.dev.prepaid.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dev.prepaid.domain.PrepaidCxOfferAdvanceFilter;

public interface PrepaidCxOfferAdvanceFilterRepository extends CrudRepository<PrepaidCxOfferAdvanceFilter, Long> {
    Optional<PrepaidCxOfferAdvanceFilter> findByOfferConfigId(@Param("offerConfigId") String offerConfigId);
    
    PrepaidCxOfferAdvanceFilter findOneByInstanceId(String instanceId);
}
