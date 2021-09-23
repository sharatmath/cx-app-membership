package com.dev.prepaid.repository;

import com.dev.prepaid.domain.PrepaidOfferMonitoringTrx;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;

public interface PrepaidOfferMonitoringTrxRepository extends CrudRepository<PrepaidOfferMonitoringTrx, Long> {
    List<PrepaidOfferMonitoringTrx> findByMsisdnAndOfferConfigId(@Param("msisdn") Long msisdn);
    List<PrepaidOfferMonitoringTrx> findByMsisdnAndCreatedDateBetween(@Param("msisdn") Long msisdn,
                                                                                      @Param("createdDate") Date endDate,
                                                                                      @Param("createdDate") Date startDate);
    @Query("select count(*) from PrepaidOfferMonitoringTrx ex where ex.msisdn = :msisdn")
    int countByOfferConfigId(@Param("msisdn") String msisdn);
    @Query(value = "SELECT count(*) FROM PREPAID_OFFER_MONITORING WHERE MSISDN =:msisdn AND CREATED_DATE >= :startDate AND CREATED_DATE <= :endDate", nativeQuery = true)
    int countByMsisdnAndCreatedDateBetween(@Param("msisdn") String msisdn,
                                                  @Param("startDate") Date startDate,
                                                  @Param("endDate") Date endDate);
}
