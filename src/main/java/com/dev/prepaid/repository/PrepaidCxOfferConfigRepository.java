package com.dev.prepaid.repository;

import com.dev.prepaid.domain.PrepaidCxOfferConfig;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PrepaidCxOfferConfigRepository extends CrudRepository<PrepaidCxOfferConfig, String> {
    Optional<PrepaidCxOfferConfig> findByInstanceId(@Param("instanceId") String instanceId);
    PrepaidCxOfferConfig findOneByInstanceIdAndDeletedDateIsNull(@Param("instanceId") String instanceId);
    Optional<PrepaidCxOfferConfig> findByOverallOfferName(@Param("overallOfferName") String overallOfferName);
}
