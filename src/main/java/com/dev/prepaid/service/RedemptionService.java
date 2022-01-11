package com.dev.prepaid.service;

import com.dev.prepaid.model.redemption.RedemptionRequest;

public interface RedemptionService {
	public void processRedemption(Long msisdn, Long offerMembershipId, String smsKeyword);
	public void redemptionQueue(RedemptionRequest redemptionRequest);
}
