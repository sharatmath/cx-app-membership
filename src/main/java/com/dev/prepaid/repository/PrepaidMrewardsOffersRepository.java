package com.dev.prepaid.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.dev.prepaid.domain.PrepaidMrewardsOffers;

@Repository
public interface PrepaidMrewardsOffersRepository extends PagingAndSortingRepository<PrepaidMrewardsOffers, Long> {
	Iterable<PrepaidMrewardsOffers> findAll();
	List<PrepaidMrewardsOffers> findAll(Sort sort);
	PrepaidMrewardsOffers findOneById(Long id);
}
