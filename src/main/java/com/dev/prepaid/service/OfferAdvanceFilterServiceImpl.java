package com.dev.prepaid.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OfferAdvanceFilterServiceImpl implements OfferAdvanceFilterService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<String> queryMsisdnByAdvanceFilter(String invocationId, String query) {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
            List<String> msisdnList = new ArrayList<>();
            for (Map row : rows) {
                msisdnList.add((String)row.get("msisdn"));
            }
            return msisdnList;
        }catch (Exception ex){
            log.error("query Advance failed", ex);
        }
        return new ArrayList<>();
    }
}
