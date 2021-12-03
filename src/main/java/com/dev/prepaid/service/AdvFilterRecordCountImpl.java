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

	private BigDecimal setSimpleJdbcCall(String procedureName, String iNSqulQUERY) {
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
		simpleJdbcCall.withProcedureName(procedureName);
		SqlParameterSource in = new MapSqlParameterSource().addValue("IN_SQL_QUERY", iNSqulQUERY);

		log.info("{}|start execute procedure|parameter|{}", in);
		Map out = simpleJdbcCall.execute(in);

		log.info("{}|result execute procedure|output|{}", out);
		return (BigDecimal) out.get("OUT_RECORD_COUNT");
	}

	private BigDecimal evaluationAdvFilterRecordCount(String procedureName, String iNSqulQUERY) {
		log.info("{}| procedureName {} ", procedureName);
		BigDecimal count = new BigDecimal(-1);
		try {

			log.info("{} procedure evaluation {} result {} ", procedureName, true);
			count = setSimpleJdbcCall(procedureName, iNSqulQUERY);

		} catch (Exception e) {
			log.error("failed", e);
		}

		return count;
	}

	@Override
	public BigDecimal getAdvFilterRecordCount(String iNSqulQUERY) {
		log.info("{}|getAdvFilterRecordCount call procedure ... ", iNSqulQUERY);
		return evaluationAdvFilterRecordCount(ADV_FILTER_GET_RECORD_COUNT, iNSqulQUERY);

	}

}
