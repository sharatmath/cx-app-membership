package com.dev.prepaid.repository;

import com.dev.prepaid.domain.PrepaidCxProvInvocations;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrepaidCxProvInvocationsRepository extends CrudRepository<PrepaidCxProvInvocations, String> {
	PrepaidCxProvInvocations findOneById(String id);
}
