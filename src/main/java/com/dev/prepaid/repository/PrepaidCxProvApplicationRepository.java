package com.dev.prepaid.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dev.prepaid.domain.PrepaidCxProvApplication;

@Repository
public interface PrepaidCxProvApplicationRepository extends CrudRepository<PrepaidCxProvApplication ,String>{

	//INSTALL //UNINSTALL //CREATE //INVOKE
	PrepaidCxProvApplication findOneByInstallIdAndAppIdAndUninstallDateIsNull(String installId, String appId);
}
