package com.dev.prepaid.model.create;

import com.dev.prepaid.model.RecordDefinition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceInstance {
	private String uuid;
	private String status;
	private String assetId;
	private String assetType;
	private String secret;
	private RecordDefinition recordDefinition;
	private ApplicationServiceInstall applicationServiceInstall;
}
