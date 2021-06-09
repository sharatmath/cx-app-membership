package com.dev.prepaid.model.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceInstanceConfiguration {
	private String uuid;
	private String status;
	private String locale;
	private String assetId;
	private String assetType;
}
