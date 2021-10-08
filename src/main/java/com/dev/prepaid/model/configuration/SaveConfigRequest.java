package com.dev.prepaid.model.configuration;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveConfigRequest {
//	private String instanceUuid;
	private Config payload;

}
