package com.dev.prepaid.repository;

import com.dev.prepaid.domain.PrepaidCxOfferEligibility;
import com.dev.prepaid.domain.PrepaidCxOfferSelection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrepaidCxOfferEligibilityRepository extends CrudRepository<PrepaidCxOfferEligibility, Long> {
    List<PrepaidCxOfferEligibility> findByOfferConfigId(@Param("offerConfigId") String offerConfigId);
}
