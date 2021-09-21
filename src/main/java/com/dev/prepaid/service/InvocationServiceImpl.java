package com.dev.prepaid.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.dev.prepaid.constant.Constant;
import com.dev.prepaid.domain.PrepaidCxOfferConfig;
import com.dev.prepaid.domain.PrepaidOfferMembership;
import com.dev.prepaid.domain.PrepaidOfferMembershipExclus;
import com.dev.prepaid.repository.PrepaidCxOfferConfigRepository;
import com.dev.prepaid.util.BaseRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dev.prepaid.InitData;
import com.dev.prepaid.domain.PrepaidCxProvInstances;
import com.dev.prepaid.model.DataRowDTO;
import com.dev.prepaid.model.imports.DataImportDTO;
import com.dev.prepaid.model.invocation.DataSet;
import com.dev.prepaid.model.invocation.InstanceContext;
import com.dev.prepaid.model.invocation.InvocationRequest;
import com.dev.prepaid.repository.PrepaidCxProvInstancesRepository;
import com.dev.prepaid.util.AppUtil;
import com.dev.prepaid.util.JwtTokenUtil;
import com.dev.prepaid.util.RESTUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InvocationServiceImpl extends BaseRabbitTemplate implements InvocationService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${eligibility.batch_size:100}")
    private int batchSize;

//	@Autowired
//	private PrepaidAppService prepaidAppService;

//	@Autowired
//	private PrepaidCxProvInstancesRepository prepaidCxProvInstancesRepository;
//

    @Autowired
    private PrepaidCxOfferConfigRepository prepaidCxOfferConfigRepository;
    @Autowired
    private RetryableService retryableService;
    @Autowired
    private OfferEligibilityService offerEligibilityService;

    //with data
    @Override
    @Async("CXInvocationExecutor")
    public void processData(InvocationRequest invocation) throws Exception {
        String instanceId = invocation.getInstanceContext().getInstanceId();
        PrepaidCxOfferConfig instanceConfiguration = prepaidCxOfferConfigRepository.findOneByInstanceIdAndDeletedDateIsNull(instanceId);
        List<List<String>> rows = invocation.getDataSet().getRows();

        if (instanceConfiguration == null) {
            throw new IllegalArgumentException("Application configuration not found, instance id : " + instanceId);
        }
        if (rows == null || rows.isEmpty()) {
            throw new IllegalArgumentException("Rows cannot be null || empty");
        }

        try {
            BigDecimal batchCount = new BigDecimal(invocation.getDataSet().getSize()).divide(new BigDecimal(batchSize));
            batchCount = batchCount.setScale(0, RoundingMode.UP);
            int count = batchCount.intValue();
            int totalObjects = invocation.getDataSet().getSize().intValue();
            //optimize
            log.info("batch size {} count {} total {}", batchSize, count, totalObjects);
            for (int i = 0; i < totalObjects; i+= batchSize) {
                if (i + batchSize > totalObjects) {
                    DataSet dataSet = new DataSet();
                    dataSet.setId(invocation.getDataSet().getId());
                    dataSet.setRows(invocation.getDataSet().getRows().subList(i, totalObjects));
                    dataSet.setSize(Long.valueOf(dataSet.getRows().size()));

                    log.info("dataSet from {} to {}", i, totalObjects);
					sendToEligibilityQueue(dataSet, invocation);

                    break;
                }
                DataSet dataSet = new DataSet();
                dataSet.setId(invocation.getDataSet().getId());
                dataSet.setRows(invocation.getDataSet().getRows().subList(i, i + batchSize));
                dataSet.setSize(Long.valueOf(batchSize));

                log.info("dataSet from {} to {}", i, i + batchSize);
				sendToEligibilityQueue(dataSet, invocation);
            }


            Instant start = Instant.now();
            log.info("invId|{}|confId|{}|start_process|{}|total_rows|{}", invocation.getUuid(), instanceConfiguration.getId(), start, rows.size());

            Duration duration = Duration.between(start, Instant.now());
            log.info("invId|{}|confId|{}|end_process|{}|total_rows|{}", invocation.getUuid(), instanceConfiguration.getId(), TimeUnit.MILLISECONDS.convert(duration), rows.size());

        } catch (Exception e) {
            // TODO: handle exception
            log.error("Exception occurred while processing rows for invocationUuid:" + invocation.getUuid(), e);
        }

        //finish
//		if(!instanceConfiguration.getNotification()) {
        //callProductOnCompletionCallbackEndpoint
        //callProductOnCompletionCallbackEndpoint(invocation);
//			retryableService.callProductOnCompletionCallbackEndpoint(invocation);
//		}else {
//			//for TESTING
//			//callProductImportEndpoint
//			callProductImportEndpoint(invocation);
//			//callProductOnCompletionCallbackEndpoint
//			callProductOnCompletionCallbackEndpoint(invocation);
//		}

    }

    private void callProductImportEndpoint(InvocationRequest invocation) {
        ResponseEntity response = null;
        InstanceContext instanceContext = invocation.getInstanceContext();
        String token = jwtTokenUtil.generateTokenProduct(invocation, instanceContext);
        String url = invocation.getProductImportEndpoint().getUrl();

        List<List<String>> rows = new ArrayList<List<String>>();

        invocation.getDataSet().getRows().forEach(row -> {
            Map<String, Object> input = invocation.getInstanceContext().getRecordDefinition().translateInputRowToMap(row);
            Map<String, Object> output = invocation.getInstanceContext().getRecordDefinition().generateOutputRowAsNewMap(input);
            List<String> listOutput = List.of(
                    output.get("appcloud_row_correlation_id").toString(), //appcloud_row_correlation_id
                    "success", //appcloud_row_status
                    "", //appcloud_row_errormessage
                    "success"); //STATUS
            rows.add(listOutput);
        });

        DataSet dataSet = DataSet.builder()
                .id(invocation.getDataSet().getId())
                .rows(rows)
                .size(null)
                .build();

        DataImportDTO data = DataImportDTO.builder()
                .fieldDefinitions(InitData.recordDefinition.getOutputParameters())
                .dataSet(dataSet)
                .build();

        response = RESTUtil.productImportPost(invocation, token, url, data, null, "application/json");
        log.debug("productImportPost response : {}", response.getStatusCode());


    }

    private void callProductOnCompletionCallbackEndpoint(InvocationRequest invocation) {
        ResponseEntity response = null;
        InstanceContext instanceContext = invocation.getInstanceContext();
        String token = jwtTokenUtil.generateTokenProduct(invocation, instanceContext);
        String url = invocation.getOnCompletionCallbackEndpoint().getUrl();

        Map<String, Object> body = new HashMap<>();
        body.put("status", "COMPLETED"); // {"status": "COMPLETED"}

        if (invocation.getOnCompletionCallbackEndpoint().getMethod().equalsIgnoreCase("PATCH")) {
            response = RESTUtil.onCompletionCallbackPatch(invocation, token, url, body, null, "application/json");
            log.debug("onCompletionCallbackPatch response : {}", response.getStatusCode());
        }
        if (invocation.getOnCompletionCallbackEndpoint().getMethod().equalsIgnoreCase("POST")) {
            response = RESTUtil.onCompletionCallbackPost(invocation, token, url, body, null, "application/json");
            log.debug("onCompletionCallbackPost response : {}", response.getStatusCode());
        }

    }

    private void sendToEligibilityQueue(DataSet dataSet, InvocationRequest invocation){
		InvocationRequest newInvocation = InvocationRequest.builder()
                .uuid(invocation.getUuid())
				.instanceContext(invocation.getInstanceContext())
				.dataSet(dataSet)
				.maxPullPageSize(invocation.getMaxPullPageSize())
				.maxPushBatchSize(invocation.getMaxPushBatchSize())
				.productImportEndpoint(invocation.getProductImportEndpoint())
				.productExportEndpoint(invocation.getProductExportEndpoint())
				.onCompletionCallbackEndpoint(invocation.getOnCompletionCallbackEndpoint())
				.build();

		HashMap<String, Object> map = new HashMap<>();
		map.put("source", "responsys");
		map.put("invocationRequest", newInvocation);
		//send to Redemption Queue
		rabbitTemplateConvertAndSendWithPriority(Constant.TOPIC_EXCHANGE_NAME_MEMBERSHIP,
				Constant.QUEUE_NAME_MEMBERSHIP_ELIGIBILITY,
				map,
				0);
	}


}
