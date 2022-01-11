package com.dev.prepaid.model.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application {
	 private String uuid;
	 private String name;
	 private String description;
	 private String baseUrl;
	 private String statusUrl;
	 private String installUrl;
	 private String configureUrl;
	 private String uninstallUrl;
	 private String saveConfigurationUrl;
	 private String smallLogo;
	 private String mediumLogo;
	 private String largeLogo;
	 private String status;
	 private String providerUuid;

}
