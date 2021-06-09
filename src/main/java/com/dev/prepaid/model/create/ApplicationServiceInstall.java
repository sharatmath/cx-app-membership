package com.dev.prepaid.model.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationServiceInstall {
	 private String uuid;
	 private String name;
	 private String description;
	 private String invokeUrl;
	 private String smallLogo;
	 private String mediumLogo;
	 private String largeLogo;
	 private Integer maxBatchSize;
	 private Application application;
	 private ServiceType serviceType;
	 private String installUuid;
	 private String productName;
	 private String providerName;
	 private String status;
}
