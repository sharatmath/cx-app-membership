package com.dev.prepaid.repository;

import com.dev.prepaid.domain.PrepaidOfferMembership;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PrepaidOfferMembershipRepository extends CrudRepository<PrepaidOfferMembership, Long> {
    List<PrepaidOfferMembership> findByMsisdnAndOfferConfigId(@Param("msisdn") Long msisdn, @Param("offerConfigId") String offerConfigId);
    List<PrepaidOfferMembership> findByMsisdnAndOfferConfigIdAndCreatedDateBetween(@Param("msisdn") Long msisdn, @Param("offerConfigId") String offerConfigId,
                                                                                   @Param("createdDate") Date endDate, @Param("createdDate") Date startDate);
    @Query("select count(*) from PrepaidOfferMembership ex where ex.offerConfigId = :offerConfigId")
    int countByOfferConfigId(@Param("offerConfigId") String offerConfigId);
    @Query(value = "SELECT count(*) FROM PREPAID_OFFER_MEMBERSHIP WHERE OFFER_CONFIG_ID =:offerConfigId AND CREATED_DATE >= :startDate AND CREATED_DATE <= :endDate", nativeQuery = true)
    int countByOfferConfigIdAndCreatedDateBetween(@Param("offerConfigId") String offerConfigId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
