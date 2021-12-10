/**
 * 
 */
package com.dev.prepaid.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

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
	
	private String ADV_FILTER_GET_NUMBERS = "ADV_FILTER_GET_NUMBERS";

	private BigDecimal setSimpleJdbcCall(String procedureName, String iNSqulQuery) {
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
		simpleJdbcCall.withProcedureName(procedureName);
		SqlParameterSource in = new MapSqlParameterSource().addValue("IN_SQL_QUERY", iNSqulQuery);

		log.info("{}|start execute procedure|parameter|{}", in);
		Map out = simpleJdbcCall.execute(in);

		log.info("{}|result execute procedure|output|{}", out);
		return (BigDecimal) out.get("OUT_RECORD_COUNT");
	}

	private BigDecimal evaluationAdvFilterRecordCount(String procedureName, String iNSqulQuery) {
		log.info("{}| procedureName {} ", procedureName);
		BigDecimal count = new BigDecimal(-1);
		try {

			log.info("{} procedure evaluation {} result {} ", procedureName, true);
			count = setSimpleJdbcCall(procedureName, iNSqulQuery);

		} catch (Exception e) {
			log.error("failed", e);
		}

		return count;
	}

	@Override
	public BigDecimal getAdvFilterRecordCount(String iNSqulQuery) {
		log.info("{}|getAdvFilterRecordCount call procedure ... ", iNSqulQuery);
		return evaluationAdvFilterRecordCount(ADV_FILTER_GET_RECORD_COUNT, iNSqulQuery);

	}
	
//	Number Query Data
	
	private String  setSimpleJdbcNumberCall(String procedureName, String iNSqulQuery) {
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
		simpleJdbcCall.withProcedureName(procedureName);
		SqlParameterSource inNum = new MapSqlParameterSource().addValue("IN_SQL_NUM_QUERY", iNSqulQuery);

		log.info("{}|start execute procedure|parameter|{}", inNum);
		Map outNum = simpleJdbcCall.execute(inNum);

		log.info("{}|result execute procedure|output|{}", outNum);
		return (String) outNum.get("IN_SQL_NUM_QUERY");
	}
	
	private String evaluationAdvFilterNumber(String procedureName, String iNSqulNumQuery) {
		log.info("{}| procedureName {} ", procedureName);
		String numbers ="";
		try {

			log.info("{} procedure evaluation {} result {} ", procedureName, true);
			numbers = setSimpleJdbcNumberCall(procedureName, iNSqulNumQuery);

		} catch (Exception e) {
			log.error("failed", e);
		}

		return numbers;
	}

	@Override
	public String getAdvFilterNumber(String iNSqulNumQuery) {
		log.info("{}|getAdvFilterRecordCount call procedure ... ", iNSqulNumQuery);
		return evaluationAdvFilterNumber(ADV_FILTER_GET_NUMBERS, iNSqulNumQuery);

	}

}
