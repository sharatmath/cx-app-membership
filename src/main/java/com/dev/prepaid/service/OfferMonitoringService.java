package com.dev.prepaid.service;

import java.util.Map;

public interface OfferMonitoringService {

    public void processUsage(Map<String, Object> payload);

    public void processTopup(Map<String, Object> payload);

    public void processArpu(Map<String, Object> payload);

}
