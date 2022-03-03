package com.dev.prepaid.repository;

import com.dev.prepaid.domain.PrepaidOfferEligibilityTrx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrepaidOfferEligibilityTrxRepository extends JpaRepository<PrepaidOfferEligibilityTrx, Long> {
    Optional<PrepaidOfferEligibilityTrx> findByInvocationIdAndBatchId(@Param("invocationId") String invocationId, @Param("batchId") Long batchId);
    List<PrepaidOfferEligibilityTrx> findByInvocationId(@Param("invocationId") String invocationId);
}
