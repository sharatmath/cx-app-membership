package com.dev.prepaid.repository;

import com.dev.prepaid.domain.PrepaidCxOfferEventCondition;
import com.dev.prepaid.domain.PrepaidCxOfferMonitoring;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PrepaidCxOfferEventConditionRepository extends CrudRepository<PrepaidCxOfferEventCondition,Long> {
    Optional<PrepaidCxOfferEventCondition> findByOfferConfigId(@Param("offerConfigId") String offerConfigId);
}
