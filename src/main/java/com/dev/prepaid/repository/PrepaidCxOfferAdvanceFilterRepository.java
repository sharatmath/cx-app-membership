package com.dev.prepaid.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dev.prepaid.domain.PrepaidCxOfferAdvanceFilter;
import com.dev.prepaid.domain.PrepaidCxOfferConfig;

public interface PrepaidCxOfferAdvanceFilterRepository extends CrudRepository<PrepaidCxOfferAdvanceFilter, Long> {
//    Optional<PrepaidCxOfferAdvanceFilter> findByOfferConfigId(@Param("instanceId") String instanceId);
    Optional<PrepaidCxOfferAdvanceFilter> findByOfferConfigId(@Param("instanceId") String instanceId);
    
    Optional<PrepaidCxOfferAdvanceFilter> findByInstanceId(@Param("instanceId") String instanceId);
}
