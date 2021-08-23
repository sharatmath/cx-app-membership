package com.dev.prepaid.service;

import com.dev.prepaid.domain.PrepaidCxOfferConfig;
import com.dev.prepaid.type.EventType;

public interface PrepaidEvaluationService {
    void evaluationPolicy(String msisdn, EventType eventType, PrepaidCxOfferConfig prepaidCxOfferConfig, String id);
}
