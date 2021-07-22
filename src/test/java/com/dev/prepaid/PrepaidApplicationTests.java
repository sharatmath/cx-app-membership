package com.dev.prepaid;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dev.prepaid.domain.PrepaidOmsOfferBucket;
import com.dev.prepaid.repository.PrepaidOmsOfferBucketRepository;
import com.dev.prepaid.repository.PrepaidCxProvInvocationsRepository;
import com.dev.prepaid.service.OfferService;
import com.dev.prepaid.util.JwtTokenUtil;

import lombok.extern.slf4j.Slf4j;

//@Slf4j
//@SpringBootTest
class PrepaidApplicationTests {
	
	@Autowired
	private OfferService offer;	

	@Autowired
	private PrepaidCxProvInvocationsRepository prepaidCxProvInvocationsRepository;	

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private PrepaidOmsOfferBucketRepository prepaidOmsOfferBucketRepository;
	@Test
	void contextLoads() throws Exception{
		
//		String url = "https://rest002.rsys8.net/rest/appcloud/v1/tenants/80578/datasets/1deef0fa-993d-48dd-9010-e8e1ca1cf216-20210325-3123508";
//		List<String> list = AppUtil.stringTokenizer(url, "/");
//		String groupId = list.get(list.size()-1);
//		System.out.println("groupId : "+groupId);
				
		//productExportEndpoint
//		String json = "{"
//				+ "    \"fieldDefinitions\": ["
//				+ "        {"
//				+ "            \"name\": \"APPCLOUD_ROW_CORRELATION_ID\","
//				+ "            \"dataType\": \"Text\","
//				+ "            \"width\": 40"
//				+ "        },"
//				+ "        {"
//				+ "            \"name\": \"CUSTOMER_ID_\","
//				+ "            \"dataType\": \"Text\","
//				+ "            \"width\": 255,"
//				+ "            \"readOnly\": true"
//				+ "        }"
//				+ "    ],"
//				+ "    \"dataSet\": {"
//				+ "        \"id\": \"f20e2e55-0980-44ce-929e-bc9052539b3b\","
//				+ "        \"size\": 10,"
//				+ "        \"rows\": ["
//				+ "            ["
//				+ "                \"126853906768;10788;1616937411\","
//				+ "                \"6587379418\""
//				+ "            ],"
//				+ "            ["
//				+ "                \"126853906788;10808;1616937411\","
//				+ "                \"6586226005\""
//				+ "            ]"
//				+ "        ]"
//				+ "    }"
//				+ "}";
//		
//		
//		
//		ObjectMapper objectMapper = new ObjectMapper();
//		log.debug("try {number1()}");
//		System.out.println("in : "+json);
//		DataExportDTO data = (DataExportDTO) GsonUtils.serializeObjectFromJSON(json, DataExportDTO.class);
//		List<List<String>> rows = data.getDataSet().getRows();
//		System.out.println("rows : "+rows);
////		DataSet data = (DataSet) GsonUtils.serializeObjectFromJSON(json, DataSet.class);
//		String dataJson = GsonUtils.deserializeObjectToJSON(data);
//		System.out.println("out : "+dataJson);
		
//		String url = "https://rest002.rsys8.net/rest/appcloud/v1/tenants/80578/datasets/2d798b30-cd20-4218-9b7a-09f307c2a96f-20210330-3123508?offset=0&limit=10000";
//		String token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhbXMiLCJhdWQiOiI2ZDI5ZWE4Ni1jMTU5LTQzZGEtYWE4Zi02OTgyYWYzMjJhYjkiLCJpYXQiOjE2MTcwMTE0MTksImV4cCI6MTYxNzAxODYxOSwiby5hLnAuY3RlbmFudElkIjoiODA1NzgifQ.dnUH21v8BSh_ZdbDT4FAD-6WeZNaO9twKgLXfIuQ8qU";
//		DataSet dataSet = DataSet.builder()
//				.id("2d798b30-cd20-4218-9b7a-09f307c2a96f")
//				.build();
//		InstanceContext instanceContext = InstanceContext.builder()
//				.instanceId("6d29ea86-c159-43da-aa8f-6982af322ab9") //OMC-ID
//				.build(); 
//		InvocationRequest invocation = InvocationRequest.builder()
//				.uuid("0eedde5d-bf18-42d9-a0d6-12f72d2e7d56") //INVOCATION-ID
//				.instanceContext(instanceContext)
//				.dataSet(dataSet)
//				.build();
//		
//		
//		//save request to nosql : PREPAID_CX_REQUEST_LOG
//		PrepaidCxProvInvocations prepaidCxProvInvocations = PrepaidCxProvInvocations.builder()
//				.id("test01")
//				.instanceId("instanceid-01")
//				.status("ON_PROGRESS")
//				.input("")
//				.output("")
//				.createdBy("prov_invocations")
//				.createdDate(new Date())
//				.build();
//		
//		System.out.println(GsonUtils.deserializeObjectToJSON(prepaidCxProvInvocations));
////		prepaidCxProvInvocationsRepository.save(prepaidCxProvInvocations);
//		
//		
////		ResponseEntity<String> responseEntity = RESTUtil.getExportData(invocation, token, url, null, String.class, "application/json");
////		
////		System.out.println(responseEntity.getBody().toString());
//		
////		System.out.println("request patch : ");
////		String url = "https://29850165-d94b-4c26-9947-462d456e69a7.mock.pstmn.io/test";
////		InvocationRequest invocation = new InvocationRequest();
////		ResponseEntity response = RESTUtil.onCompletionCallbackPatchTest(invocation, "", url, null, null, "application/json");
////		System.out.println("response : "+response.getStatusCodeValue());
//		
//		List<List<String>> rows = new ArrayList<List<String>>();
//		List<String> listOutput = List.of("appcloudcorid1","success","","success");
//		rows.add(listOutput);
//		
//		DataSet dataSet = DataSet.builder()
//				.id("2d798b30-cd20-4218-9b7a-09f307c2a96f")
//				.rows(rows)
//				.build();
//		InstanceContext instanceContext = InstanceContext.builder()
//				.appId("app-id")
//				.installId("install-id")
//				.instanceId("instance-id") //OMC-ID
//				.tenantId("tenant-id")
//				.secret("16620479-5822-4403-a53e-ac42d1a1abbe-16d5232a-0e6b-4fa1-b800-3208be76c0f3")
//				.recordDefinition(InitData.recordDefinition)
//				.build(); 
//		ProductExpImpEndpoint productImpEndpoint = ProductExpImpEndpoint.builder()
//				.url("http://url")
//				.method("POST")
//				.build();
//		InvocationRequest invocation = InvocationRequest.builder()
//				.uuid("0eedde5d-bf18-42d9-a0d6-12f72d2e7d56") //INVOCATION-ID
//				.instanceContext(instanceContext)
//				.dataSet(dataSet)
//				.productImportEndpoint(productImpEndpoint)
//				.build();
//		
////		System.out.println(GsonUtils.deserializeObjectToJSON(invocation));
//		
////		callProductImportEndpoint(invocation);
//		
//	}
//	
//	private void callProductImportEndpoint(InvocationRequest invocation) {
//		ResponseEntity response = null;
//		InstanceContext instanceContext = invocation.getInstanceContext();
//		String token = jwtTokenUtil.generateTokenProduct(invocation, instanceContext);
//		String url = invocation.getProductImportEndpoint().getUrl();
//		
//		List<List<String>> rows = new ArrayList<List<String>>();
//		
//		invocation.getDataSet().getRows().forEach(row -> {
//			Map<String, Object> input = invocation.getInstanceContext().getRecordDefinition().translateInputRowToMap(row);
//			Map<String, Object> output = invocation.getInstanceContext().getRecordDefinition().generateOutputRowAsNewMap(input);
//			List<String> listOutput = List.of(
//					output.get("appcloud_row_correlation_id").toString(), //appcloud_row_correlation_id
//					"success", //appcloud_row_status
//					"", //appcloud_row_errormessage
//					"success"); //STATUS
//			System.out.println(listOutput); 
//			rows.add(listOutput);
//		});
//		
//		DataSet dataSet = DataSet.builder()
//				.id(invocation.getDataSet().getId())
//				.rows(rows)
//				.size(null)
//				.build();
//		
//		DataImportDTO data = DataImportDTO.builder()
//				.fieldDefinitions(InitData.recordDefinition.getOutputParameters())
//				.dataSet(dataSet)
//				.build();
//	    
//	    response = RESTUtil.productImportPost(invocation, token, url, data, null, "application/json");
//    	log.debug("productImportPost response : {}",response.getStatusCode());	    	
//    	
//	    
	PrepaidOmsOfferBucket prepaid_oms = prepaidOmsOfferBucketRepository.findOneByCode("6000151");
	
	System.out.println("\n result:" + prepaid_oms);
	}

}
