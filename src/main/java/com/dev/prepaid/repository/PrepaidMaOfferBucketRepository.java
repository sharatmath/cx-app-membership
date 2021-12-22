package com.dev.prepaid.repository;

import com.dev.prepaid.domain.PrepaidDaOfferBucket;
import com.dev.prepaid.domain.PrepaidMaCreditOffer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PrepaidMaOfferBucketRepository extends CrudRepository<PrepaidMaCreditOffer, Long> {
    List<PrepaidMaCreditOffer> findAll();
    PrepaidMaCreditOffer findOneById(Long id);

    @Query("select ma from PrepaidMaCreditOffer ma where ma.productName = :code")
    PrepaidMaCreditOffer findOneByCode(String code);

    public List<PrepaidMaCreditOffer> findAllByProductName(String productName);

    public List<PrepaidMaCreditOffer> findAllById(Long id);
}
