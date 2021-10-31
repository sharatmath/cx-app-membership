package com.dev.prepaid.repository;

import com.dev.prepaid.domain.PrepaidDaOfferBucket;
import com.dev.prepaid.domain.PrepaidMaCreditOffer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PrepaidMaOfferBucketRepository extends CrudRepository<PrepaidMaCreditOffer, Long> {
    List<PrepaidMaCreditOffer> findAll();
    PrepaidMaCreditOffer findOneById(Long id);
    PrepaidMaCreditOffer findOneByCode(String code);
}
