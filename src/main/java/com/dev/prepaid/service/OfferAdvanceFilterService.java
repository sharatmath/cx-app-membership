package com.dev.prepaid.service;

import java.util.List;

public interface OfferAdvanceFilterService {

    public List<String> queryMsisdnByAdvanceFilter(String invocationId, String query);
}
