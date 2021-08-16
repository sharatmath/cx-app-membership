package com.dev.prepaid.model.invocation;

import com.dev.prepaid.model.RecordDefinition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstanceContext {
	 private String appId;
	 private String appVersion;
	 private String installId;
	 private String instanceId;
	 private String serviceId;
	 private String tenantId;
	 private String productId;
	 private Integer maxPushBatchSize;
	 private String secret;
	 RecordDefinition recordDefinition;
	 private Integer maxBatchSize;
	 
}
