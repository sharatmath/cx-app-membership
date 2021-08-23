package com.dev.prepaid.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CxAppsInfo {

	private String invocationId;
	private String groupId;
	private String productImportEndpoint;
	private String productOnCompletionCallbackEndpoint;
	private Long dataSetSize;
	private String iss;
	private String sub;
	private String aud; //instanceId
	private String tenantId;
	private String secret;
}
