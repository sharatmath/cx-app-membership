package com.dev.prepaid.repository;

import com.dev.prepaid.domain.PrepaidOfferEligibilityTrx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrepaidOfferEligibilityTrxRepository extends JpaRepository<PrepaidOfferEligibilityTrx, Long> {


}
