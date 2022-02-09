package com.dev.prepaid.repository;

import com.dev.prepaid.domain.PrepaidCxOfferAdvanceFilter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrepaidCxOfferAdvanceFilterRepository extends CrudRepository<PrepaidCxOfferAdvanceFilter, Long> {
    Optional<PrepaidCxOfferAdvanceFilter> findByOfferConfigId(@Param("offerConfigId") String offerConfigId);
}
