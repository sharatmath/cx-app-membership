package com.dev.prepaid;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.dev.prepaid.model.Parameters;
import com.dev.prepaid.model.RecordDefinition;
import com.dev.prepaid.model.create.ServiceInstance;
import com.dev.prepaid.model.install.AppInstall;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Component
public class InitData {
	static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
	public static RecordDefinition recordDefinition;
	public static Map<String, ServiceInstance> serviceInstances;
	public static Map<String, AppInstall> appInstalls;
		
	@Value("${tasks.scheduled.zone}")
	String zoneId;

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone(zoneId));   // It will set timezone
        log.info("Spring boot application running in SGT timezone :"+new Date());
		createDefaultProvisionRecordDefinition();
		
	}
		
	
	/// PROVISION
	public void createDefaultProvisionRecordDefinition() {
		RecordDefinition recordDef = new RecordDefinition();
		recordDef.setInputParameters(inputDefaultProvision());
		recordDef.setOutputParameters(outputDefaultProvision());
		
		recordDefinition = recordDef;
	}
	
	private ArrayList<Parameters> inputDefaultProvision(){
		ArrayList<Parameters> inParameters = new ArrayList<Parameters>();
		Parameters inputParameter = new Parameters();
		
		// INPUT DEFAULT
		inputParameter = Parameters.builder()
						 .name("appcloud_row_correlation_id")
						 .dataType("Text")
						 .width(40)
						 .unique(true)
						 .required(true)
						 .build();
		inParameters.add(inputParameter);		
		
		
		inputParameter = Parameters.builder()
						 .name("CUSTOMER_ID_")
						 .dataType("Text")
						 .width(40)
						 .required(true)
						 .readOnly(true)
						 .build();
		inParameters.add(inputParameter); 	
		return inParameters;
	}
	
	private ArrayList<Parameters> outputDefaultProvision(){
		ArrayList<Parameters> outParameters = new ArrayList<Parameters>();		 
		Parameters outputParameter = new Parameters();

		// OUTPUT DEFAULT
		outputParameter = Parameters.builder()
				 .name("appcloud_row_correlation_id")
				 .dataType("Text")
				 .width(40)
				 .readOnly(false)
//				 .unique(true)
//				 .required(true)
				 .build();
		outParameters.add(outputParameter);			 
		outputParameter = Parameters.builder()
				 .name("appcloud_row_status")
				 .dataType("Text")
				 .width(40)
				 .readOnly(false)
//				 .required(true)
				 .build();		 
		outParameters.add(outputParameter);
		outputParameter = Parameters.builder()
				 .name("appcloud_row_errormessage")
				 .dataType("Text")
				 .width(255)
				 .readOnly(false)
				 .build();
		outParameters.add(outputParameter);	
		
		
		outputParameter = Parameters.builder()
				 .name("STATUS")
				 .dataType("Text")
				 .width(40)
				 .readOnly(false)
				 .build();		 
		outParameters.add(outputParameter);
		return outParameters;
	}
	
}
