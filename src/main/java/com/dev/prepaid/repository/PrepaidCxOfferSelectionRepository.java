package com.dev.prepaid.repository;

import com.dev.prepaid.domain.PrepaidCxOfferConfig;
import com.dev.prepaid.domain.PrepaidCxOfferSelection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PrepaidCxOfferSelectionRepository extends CrudRepository<PrepaidCxOfferSelection, Long> {
    List<PrepaidCxOfferSelection> findByOfferConfigId(@Param("offerConfigId") String offerConfigId);
}
