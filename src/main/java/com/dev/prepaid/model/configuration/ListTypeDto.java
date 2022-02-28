package com.dev.prepaid.model.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListTypeDto {
	private Long whitelistId;
	private String whitelistType;
}
