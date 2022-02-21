package com.dev.prepaid.service;

import com.dev.prepaid.domain.PrepaidCxOfferConfig;
import com.dev.prepaid.model.invocation.InvocationRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface OfferEligibilityService {
    public List<List<String>> processData(
            List<List<String>> rows,
            InvocationRequest invocation,
            InvocationRequest invocationOri,
            PrepaidCxOfferConfig instanceConfiguration,
            String groupId,
            Long dataSetSize) throws Exception;

    public List<List<String>> evaluationSubscriberExclusion(List<List<String>> rows, InvocationRequest invocation, PrepaidCxOfferConfig instanceConfiguration) throws Exception;

    public List<List<String>> evaluationSubscriberLevel(List<List<String>> rows, InvocationRequest invocation, PrepaidCxOfferConfig instanceConfiguration) throws Exception;

    public List<List<String>> evaluationAdvanceFilter(List<List<String>> rows, InvocationRequest invocation, PrepaidCxOfferConfig instanceConfiguration) throws Exception;

    public List<List<String>> evaluationOfferLevelCondition(List<List<String>> rows, InvocationRequest invocation, PrepaidCxOfferConfig instanceConfiguration) throws Exception;

    public void productImportEndpoint(List<List<String>> rows, InvocationRequest invocation, InvocationRequest invocationOri,  PrepaidCxOfferConfig instanceConfiguration) throws Exception;

    public ResponseEntity<String> sendToRedemptionQueue(String invId, Map<String, Object> payload);
}