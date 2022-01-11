package com.dev.prepaid.service;

import com.dev.prepaid.model.configuration.SaveConfigRequest;
import com.dev.prepaid.model.create.ServiceInstance;
import com.dev.prepaid.model.install.AppInstall;

public interface PrepaidCxService {
	
	public void appInstallAddEntity(AppInstall appInstall);
	
	public void appCreateAddEntity(ServiceInstance serviceInstance);
	
	public void appConfigurationAddEntity(SaveConfigRequest saveConfigRequest);

	public void appDeleteEntity(String instanceUuid);

	public void appUninstallEntity(String installId, String appId);

}
