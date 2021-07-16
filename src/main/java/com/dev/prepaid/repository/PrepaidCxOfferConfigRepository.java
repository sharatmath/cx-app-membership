package com.dev.prepaid.repository;

import com.dev.prepaid.domain.PrepaidCxOfferConfig;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface PrepaidCxOfferConfigRepository extends CrudRepository<PrepaidCxOfferConfig, String> {
    Optional<PrepaidCxOfferConfig> findByInstanceId(@Param("instanceId") String instanceId);
//    //CREATE
//    PrepaidCxOfferConfig findOneByServiceIdAndInstanceIdAndDeletedDateIsNull(String serviceId, String instanceId);
//    PrepaidCxOfferConfig findOneByApplicationIdAndServiceIdAndInstanceIdAndDeletedDateIsNull
//            (String applicationId, String serviceId, String instanceId);

    //CONFIGURE //INVOKE //DELETE
    PrepaidCxOfferConfig findOneByInstanceIdAndDeletedDateIsNull(@Param("instanceId") String instanceId);
}
