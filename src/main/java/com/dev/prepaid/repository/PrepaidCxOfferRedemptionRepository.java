package com.dev.prepaid.repository;

import com.dev.prepaid.domain.PrepaidCxOfferRedemption;
import com.dev.prepaid.domain.PrepaidCxOfferSelection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PrepaidCxOfferRedemptionRepository extends CrudRepository<PrepaidCxOfferRedemption, Long> {
    Optional<PrepaidCxOfferRedemption> findByOfferConfigId(@Param("offerConfigId") String offerConfigId);
}
