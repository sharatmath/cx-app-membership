package com.dev.prepaid.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dev.prepaid.constant.Constant;
import com.dev.prepaid.model.redemption.RedemptionRequest;
import com.dev.prepaid.model.redemption.RedemptionResponse;
import com.dev.prepaid.service.OfferService;
import com.dev.prepaid.service.RedemptionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/redemption/")
public class RedemptionController {
	
	@Autowired
	private RedemptionService redemptionService;
	
	@Autowired
	private OfferService offerService;
	
	@RequestMapping(value = "redemptionqueue",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RedemptionResponse> redemptionQueue(@RequestBody RedemptionRequest redemReq){
		
		redemptionService.redemptionQueue(redemReq);
		
		RedemptionResponse response = RedemptionResponse.builder()
				.successful(true)
				.content(redemReq.toString())
				.errorMessage("")
				.build();
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
}
