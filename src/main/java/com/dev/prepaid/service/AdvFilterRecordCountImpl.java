/**
 * 
 */
package com.dev.prepaid.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import com.dev.prepaid.controller.AdvFilterRecordCount;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Saket
 *
 * 
 */

@Slf4j
@Service
public class AdvFilterRecordCountImpl implements AdvFilterRecordCountServices {

//	@Autowired
	SimpleJdbcCall simpleJdbcCall;
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	AdvFilterRecordCountServices advFilterRecordCountServices;

	private String ADV_FILTER_GET_RECORD_COUNT = "ADV_FILTER_GET_RECORD_COUNT";

	private void setSimpleJdbcCall(String procedureName) {
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
		simpleJdbcCall.withProcedureName(procedureName);
		SqlParameterSource in = new MapSqlParameterSource().addValue("in_request_id", procedureName);

		log.info("{}|start execute procedure|parameter|{}", in);
		Map out = simpleJdbcCall.execute(in);
		log.info("{}|result execute procedure|output|{}", out);
	}

	private void evaluationEventCondition(String procedureName) {
		log.info("{}| procedureName {} ", procedureName);
		try {
			setSimpleJdbcCall(procedureName);
			log.info("{} procedure evaluation {} result {} ", true);

		} catch (Exception e) {
			log.error("failed", e);
		}
	}

	@Override
	public String getAdvFilterRecordCount(String IN_SQL_QUERY) {
		log.info("{}|getAdvFilterRecordCount call procedure ... ", IN_SQL_QUERY);
		evaluationEventCondition(ADV_FILTER_GET_RECORD_COUNT);
		return IN_SQL_QUERY;
	}

}
