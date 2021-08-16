package com.dev.prepaid.model.invocation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvocationResponseContent {
	private String invocationId;
	 private String instanceId;
}
