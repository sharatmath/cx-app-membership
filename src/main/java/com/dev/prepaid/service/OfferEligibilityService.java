package com.dev.prepaid.service;

import com.dev.prepaid.domain.PrepaidCxOfferConfig;
import com.dev.prepaid.model.DataRowDTO;
import com.dev.prepaid.model.invocation.InvocationRequest;
import com.dev.prepaid.repository.PrepaidCxOfferEligibilityRepository;
import com.dev.prepaid.repository.PrepaidCxOfferSelectionRepository;
import com.dev.prepaid.repository.PrepaidOfferMembershipExclusRepository;
import com.dev.prepaid.repository.PrepaidOfferMembershipRepository;

import java.util.List;

public interface OfferEligibilityService {
    public List<List<String>> processData(
            List<List<String>> rows,
            InvocationRequest invocation,
            PrepaidCxOfferConfig instanceConfiguration,
            String groupId,
            Long dataSetSize) throws Exception;
}