package com.dev.prepaid.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dev.prepaid.domain.PrepaidOmsOfferBucket;

@Repository
public interface PrepaidOmsOfferBucketRepository extends CrudRepository<PrepaidOmsOfferBucket, Long>{
	
//	List<PrepaidOmsOfferBucket> findAllByDeletedDateIsNull();
	List<PrepaidOmsOfferBucket> findAll();
	PrepaidOmsOfferBucket findOneById(Long id);

	PrepaidOmsOfferBucket findOneByCode(String code);
	
}
