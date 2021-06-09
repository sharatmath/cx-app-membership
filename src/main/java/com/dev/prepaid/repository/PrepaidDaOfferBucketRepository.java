package com.dev.prepaid.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dev.prepaid.domain.PrepaidDaOfferBucket;

@Repository
public interface PrepaidDaOfferBucketRepository extends CrudRepository<PrepaidDaOfferBucket, Long> {

//	List<PrepaidDaOfferBucket> findAllByDeletedDateIsNull();
	List<PrepaidDaOfferBucket> findAll();
	PrepaidDaOfferBucket findOneById(Long id);

	PrepaidDaOfferBucket findOneByCode(String code);
}
