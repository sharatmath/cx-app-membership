package com.dev.prepaid.model.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateResponse {
	private String id;
	private String serviceId;
	private String status;
	private String name;
	private String iconUrl;

}
