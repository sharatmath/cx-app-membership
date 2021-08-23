package com.dev.prepaid.model.install;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppInstall {
	private ApplicationInstall applicationInstall;
	private String secret;
}
