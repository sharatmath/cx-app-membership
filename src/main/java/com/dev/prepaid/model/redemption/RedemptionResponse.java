package com.dev.prepaid.model.redemption;

import com.dev.prepaid.model.invocation.InvocationResponseContent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedemptionResponse {
	private Boolean successful;
	private String content;
	private String errorMessage;
}
