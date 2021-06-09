package com.dev.prepaid.model.install;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationInstall {
	private String uuid;
	private Integer deleted;
	private Application application;
	private String status;
}
