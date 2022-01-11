package com.dev.prepaid.model.configuration;

import java.util.Date;

import com.dev.prepaid.model.RecordDefinition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveConfigResponse {
	private String configurationStatus;
	private String payload;
	private String httpStatusCode;
	private RecordDefinition recordDefinition;
}
