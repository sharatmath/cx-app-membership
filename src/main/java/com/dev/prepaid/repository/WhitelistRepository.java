package com.dev.prepaid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.prepaid.domain.Whitelist;


@Repository
public interface WhitelistRepository extends JpaRepository<Whitelist, Long> {
}
