package com.dev.prepaid.repository;

import com.dev.prepaid.domain.PrepaidOfferMembership;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrepaidOfferMembershipRepository extends CrudRepository<PrepaidOfferMembership, Long> {
    List<PrepaidOfferMembership> findByMsisdnAndOfferConfigId(@Param("msisdn") Long msisdn, @Param("offerConfigId") String offerConfigId);
}
