package com.dev.prepaid.model.install;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application {
	private String baseUrl;
	private String configureUrl;
	private String description;
	private Integer deleted;
	private String installUrl;
	private String largeLogo;
	private String mediumLogo;
	private String name;
	private String saveConfigurationUrl;
	private String smallLogo;
	private String status;
	private String publicationStatus;
	private StatusMessage statusMessage;
	private String statusUrl;
	private String uninstallUrl;
	private String uuid;
	private String providerUuid;
}
