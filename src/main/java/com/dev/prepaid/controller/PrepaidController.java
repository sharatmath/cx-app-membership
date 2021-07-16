package com.dev.prepaid.controller;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.dev.prepaid.model.configuration.OfferSelection;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dev.prepaid.InitData;
import com.dev.prepaid.domain.PrepaidCxProvInvocations;
import com.dev.prepaid.model.configuration.SaveConfigRequest;
import com.dev.prepaid.model.configuration.SaveConfigResponse;
import com.dev.prepaid.model.create.CreateResponse;
import com.dev.prepaid.model.create.ServiceInstance;
import com.dev.prepaid.model.install.AppInstall;
import com.dev.prepaid.model.invocation.InvocationRequest;
import com.dev.prepaid.model.invocation.InvocationResponse;
import com.dev.prepaid.model.invocation.InvocationResponseContent;
import com.dev.prepaid.model.status.Info;
import com.dev.prepaid.model.status.Service;
import com.dev.prepaid.model.status.StatusResponse;
import com.dev.prepaid.repository.PrepaidCxProvInvocationsRepository;
import com.dev.prepaid.service.InvocationService;
import com.dev.prepaid.service.PrepaidCxService;
import com.dev.prepaid.util.AppUtil;
import com.dev.prepaid.util.GUIDUtil;
import com.dev.prepaid.util.GsonUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/")
public class PrepaidController {
	
	@Autowired
	private InvocationService invocationService;
	
	@Autowired
	private PrepaidCxService prepaidCxService;
		
	@Autowired
	private PrepaidCxProvInvocationsRepository prepaidCxProvInvocationsRepository;
	
	@Autowired
	private HealthEndpoint health;
	
	
	@RequestMapping(value = "install", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void install(@RequestBody AppInstall appInstall){
		log.info("install call");
		log.info(GsonUtils.deserializeObjectToJSON(appInstall));
		
		prepaidCxService.appInstallAddEntity(appInstall);
	}	
	
	@RequestMapping(value = "create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<CreateResponse> create(@RequestBody ServiceInstance serviceInstance){
		log.info("create call");
		log.info(GsonUtils.deserializeObjectToJSON(serviceInstance));
		
		if (serviceInstance == null) {
			throw new IllegalArgumentException("Empty Service Instance");
		}
		
		// If the app is up, the app creates an instance and responds with success or failure
		// If the app is not up, the app does not respond
		if(serviceInstance.getApplicationServiceInstall().getStatus().equalsIgnoreCase("UP")) {
			prepaidCxService.appCreateAddEntity(serviceInstance);
			
			CreateResponse response = new CreateResponse().builder()
					.id(serviceInstance.getUuid()) //appInstall uuid
					.serviceId(serviceInstance.getApplicationServiceInstall().getUuid()) //appServiceInstall uuid
					.status("CREATED")
					.name(null)
					.iconUrl(null)
					.build();
			
			return new ResponseEntity<CreateResponse>(response, HttpStatus.OK);
		}else {
			
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
		
	}
	
	@PostMapping(value = "saveconfiguration")
	public SaveConfigResponse saveConfiguration(@RequestBody SaveConfigRequest request) throws Exception{ //Config config
		log.info("saveconfiguration call");
		log.info(GsonUtils.deserializeObjectToJSON(request));

//			String offerBucketType = AppUtil.stringTokenizer(request.getPayload().getOfferBucketId(), "|").get(0);
//			String bucketOfferId = AppUtil.stringTokenizer(request.getPayload().getOfferBucketId(), "|").get(1);

		request.getPayload().getOfferSelections().stream().forEach(o ->
				o.setOfferBucketType(AppUtil.stringTokenizer(o.getOfferBucketId(), "|").get(0))
		);

		request.getPayload().getOfferSelections().stream().forEach(o ->
				o.setOfferBucketId(AppUtil.stringTokenizer(o.getOfferBucketId(), "|").get(1))
		);

		log.debug(request.getPayload().toString());
		prepaidCxService.appConfigurationAddEntity(request);
				
		request.getPayload().setUuid(GUIDUtil.generateGUID());
		SaveConfigResponse response = new SaveConfigResponse().builder()
				.configurationStatus("CONFIGURED")
				.payload(GsonUtils.deserializeObjectToJSON(request.getPayload()))
				.httpStatusCode("200")
				.recordDefinition(InitData.recordDefinition)
				.build();
		
		return response;
	}
	
	
	@RequestMapping(value = "invoke",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InvocationResponse> invoke(@RequestBody InvocationRequest req){
		log.info("invoke call");	
		log.info(GsonUtils.deserializeObjectToJSON(req));
		

		String invocationId = GUIDUtil.generateGUID();
		req.setUuid(invocationId);
		try {		
			//save request to nosql : PREPAID_CX_REQUEST_LOG
			PrepaidCxProvInvocations prepaidCxProvInvocations = PrepaidCxProvInvocations.builder()
					.id(invocationId)
					.instanceId(req.getInstanceContext().getInstanceId())
					.status("ON_PROGRESS")
					.input(GsonUtils.deserializeObjectToJSON(req))
					.output("")
//					.createdBy("prov_invocations")
					.createdDate(new Date())
					.build();
			prepaidCxProvInvocationsRepository.save(prepaidCxProvInvocations);
			
			// Async
			// Invoke without data will have DataSet with size, but no rows in DataSet
			if (req.getDataSet().getRows() == null || req.getDataSet().getRows().isEmpty()) {
				log.debug("rows null");
				// Async
				// Export DataSet rows from Product
				// exportProcessAndImportData(invocation, serviceInvocationDto);
				invocationService.exportProcessAndImportData(req);
			} else {
				// Async
				// Process data and Import data to Product
				invocationService.processData(req);
			}
			
			InvocationResponseContent responseContent = InvocationResponseContent.builder()
					.instanceId(req.getInstanceContext().getInstanceId())
					.invocationId(req.getUuid())
					.build();
			
			InvocationResponse response = InvocationResponse.builder()
					.successful(true)
					.content(responseContent)
					.errorMessage("")
					.build();
			
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}catch(Exception e) {
			e.printStackTrace();
			InvocationResponseContent responseContent = InvocationResponseContent.builder()
					.instanceId(req.getInstanceContext().getInstanceId())
					.invocationId(req.getUuid())
					.build();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvocationResponse(false, responseContent, e.getMessage()));
		}
	}	
	
	
	
	
	
	
	@GetMapping("status")
	public StatusResponse status() {
		
		String error = null;
		HealthComponent healthComponent = health.health();
		
		//DB Checking
		try {
			JSONObject healthObject = new JSONObject(GsonUtils.deserializeObjectToJSON(healthComponent));
			error = healthObject.getJSONObject("components").getJSONObject("db").getJSONObject("details").getString("error");			
		} catch (JSONException e) { log.debug(e.getMessage()); }
		//RabbitMQ Checking
		try {
			JSONObject healthObject = new JSONObject(GsonUtils.deserializeObjectToJSON(healthComponent));
			error = healthObject.getJSONObject("components").getJSONObject("rabbit").getJSONObject("details").getString("error");			
		} catch (JSONException e) { log.debug(e.getMessage()); }
		
		
		StatusResponse response = StatusResponse.builder()
				.warnings(Collections.EMPTY_LIST)
				.errors(Collections.EMPTY_LIST)
				.build();
		
		Info info = Info.builder()
				.name("CX-APP Provisioning")
				.build();		

		Service service = Service.builder()
				.info(info)
				.warnings(Collections.EMPTY_LIST)
				.errors(Collections.EMPTY_LIST)
				.build();
		
		if(error != null) service.setErrors(List.of(error));
		if(error != null) response.setErrors(List.of(error));
		
		response.setInfo(info);
		response.setServices(List.of(service));
		
		return response;
		
	}
	
	@RequestMapping(value = "delete", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteServiceInstance(@RequestBody String body) throws JSONException {
		log.info("delete call");
		log.info(GsonUtils.deserializeObjectToJSON(body));
		
		JSONObject request  = new JSONObject(body);
		String instanceUuid = request.getString("uuid");
		
		prepaidCxService.appDeleteEntity(instanceUuid);
				
	}
	
	@RequestMapping(value = "uninstall", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void uninstall(@RequestBody String body) throws JSONException{
		log.info("uninstall call");
		log.info(GsonUtils.deserializeObjectToJSON(body));
		
		JSONObject request  = new JSONObject(body);
		String installId = request.getString("uuid");
		String appId = request.getJSONObject("application").getString("uuid");
		
		prepaidCxService.appUninstallEntity(installId, appId);
				
	}
	
	
}
