package com.dev.prepaid.model.redemption;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedemptionRequest {
	private Long offerMembershipId;
	private Long msisdn;
	private String smsKeyword;
	private String instanceId;
}
