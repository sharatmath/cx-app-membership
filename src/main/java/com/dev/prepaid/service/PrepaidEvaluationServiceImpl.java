package com.dev.prepaid.service;

import com.dev.prepaid.domain.PrepaidCxOfferConfig;
import com.dev.prepaid.type.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class PrepaidEvaluationServiceImpl implements PrepaidEvaluationService {

    @Autowired
    EntityManager entityManager;
    @Autowired
    private Environment env;
    private String PROC_MONITORING_USAGE = "prepaid_evaluation_monitoring_usage_policy";
    private String PROC_MONITORING_TOPUP = "prepaid_evaluation_monitoring_topup_policy";
    private String PROC_MONITORING_ARPU = "prepaid_evaluation_monitoring_arpu_policy";
    SimpleJdbcCall simpleJdbcCall;
    @Autowired
    JdbcTemplate jdbcTemplate;


    public void evaluationPolicy(String msisdn, EventType eventType, PrepaidCxOfferConfig prepaidCxOfferConfig, String requestId) {
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);

        if (EventType.USAGE.equals(eventType)) {
            simpleJdbcCall
                    .withProcedureName(PROC_MONITORING_USAGE);
        } else if (EventType.ARPU.equals(eventType)) {
            simpleJdbcCall
                    .withProcedureName(PROC_MONITORING_ARPU);
        } else if (EventType.TOPUP.equals(eventType)) {
            simpleJdbcCall
                    .withProcedureName(PROC_MONITORING_TOPUP);
        }


        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("in_request_id", requestId)
                .addValue("in_instance_id", prepaidCxOfferConfig.getInstanceId())
                .addValue("in_offer_config_id", prepaidCxOfferConfig.getId())
                .addValue("in_provision_type", prepaidCxOfferConfig.getProvisionType())
                .addValue("in_event_type", eventType.getDescription())
                .addValue("in_msisdn", msisdn);

        Optional result = Optional.empty();

        try {

            log.info("{}|start execute procedure|parameter|{}", requestId, in);
            Map out = simpleJdbcCall.execute(in);
            log.info("{}|result execute procedure|output|{}", requestId, out);
            if (out != null) {
                BigDecimal out_result = (BigDecimal) out.get("out_result");
                if (out_result.intValue() == 1)
                    log.info("{} procedure evaluation {} eventType {} result {} ", requestId, msisdn, eventType, true);
                else
                    log.info("{} procedure evaluation {} eventType {} result {} ", requestId, msisdn, eventType, false);

            }

        } catch (Exception e) {
            log.error("failed", e);
        }
    }
}
