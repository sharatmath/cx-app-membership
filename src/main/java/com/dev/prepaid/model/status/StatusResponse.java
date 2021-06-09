package com.dev.prepaid.model.status;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponse {
	 private Info info;
	 private List<Object> warnings;
	 private List<Object> errors;
	 private List<Object> services;
}
