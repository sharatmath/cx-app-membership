package com.dev.prepaid.repository;

import com.dev.prepaid.domain.PrepaidCxOfferEligibility;
import com.dev.prepaid.domain.PrepaidCxOfferSelection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PrepaidCxOfferEligibilityRepository extends CrudRepository<PrepaidCxOfferEligibility, Long> {
    Optional<PrepaidCxOfferEligibility> findByOfferConfigId(@Param("offerConfigId") String offerConfigId);
}
