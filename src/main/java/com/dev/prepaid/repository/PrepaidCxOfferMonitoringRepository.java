package com.dev.prepaid.repository;

import com.dev.prepaid.domain.PrepaidCxOfferMonitoring;
import com.dev.prepaid.domain.PrepaidCxOfferSelection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PrepaidCxOfferMonitoringRepository extends CrudRepository<PrepaidCxOfferMonitoring, Long> {
    Optional<PrepaidCxOfferMonitoring> findByOfferConfigId(@Param("offerConfigId") String offerConfigId);
}
