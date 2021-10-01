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
import com.dev.prepaid.domain.*;
import com.dev.prepaid.model.invocation.*;
import com.dev.prepaid.repository.PrepaidCxOfferConfigRepository;
import com.dev.prepaid.repository.PrepaidOfferEligibilityTrxRepository;
import com.dev.prepaid.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dev.prepaid.InitData;
import com.dev.prepaid.model.DataRowDTO;
import com.dev.prepaid.model.imports.DataImportDTO;
import com.dev.prepaid.repository.PrepaidCxProvInstancesRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InvocationServiceImpl extends BaseRabbitTemplate implements InvocationService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${eligibility.batch_size:100}")
    private int batchSize;
    @Autowired
    private PrepaidCxOfferConfigRepository prepaidCxOfferConfigRepository;
    @Autowired
    private RetryableService retryableService;
    @Autowired
    private OfferEligibilityService offerEligibilityService;
    @Autowired
    private PrepaidOfferEligibilityTrxRepository prepaidOfferEligibilityTrxRepository;

    @Override
    @Async("CXInvocationExecutor")
    public void invoke(InvocationRequest invocation) throws Exception {
        String instanceId = invocation.getInstanceContext().getInstanceId();
        PrepaidCxOfferConfig instanceConfiguration = prepaidCxOfferConfigRepository.findOneByInstanceIdAndDeletedDateIsNull(instanceId);
        List<List<String>> rows = invocation.getDataSet().getRows();
        // Async
        // Invoke without data will have DataSet with size, but no rows in DataSet
        if (invocation.getDataSet().getRows() == null || invocation.getDataSet().getRows().isEmpty()) {
            log.debug("invoke without data, rows null");
            BigDecimal batchCount = new BigDecimal(invocation.getDataSet().getSize()).divide(new BigDecimal(batchSize));
            batchCount = batchCount.setScale(0, RoundingMode.UP);
            int count = batchCount.intValue();
            int totalObjects = invocation.getDataSet().getSize().intValue();
            log.info("batch size {} count {} total {}", batchSize, count, totalObjects);
            int countBatch = 0;
            for (int i = 0; i < totalObjects; i += batchSize) {
                int limit = batchSize;
                int offset = countBatch * batchSize;
                log.info("========================================START BATCH {}=====================================",
                        countBatch);
                ProductExportEndpointResponse response = callProductExportEndpoint(invocation, limit, offset);
                log.info("BATCH RESPONSE {}",response);
                InstanceContext newInstanceContext = InstanceContext
                        .builder()
                        .instanceId(invocation.getInstanceContext().getInstanceId())
                        .recordDefinition(invocation.getInstanceContext().getRecordDefinition())
                        .appId(invocation.getInstanceContext().getAppId())
                        .appVersion(invocation.getInstanceContext().getAppVersion())
                        .installId(invocation.getInstanceContext().getInstallId())
                        .maxBatchSize(invocation.getInstanceContext().getMaxBatchSize())
                        .productId(invocation.getInstanceContext().getProductId())
                        .secret(invocation.getInstanceContext().getSecret())
                        .build();
                InvocationRequest newInvocationPerBatch = InvocationRequest
                        .builder()
                        .dataSet(response.getDataSet()) // new
                        .instanceContext(newInstanceContext) // new
                        .uuid(invocation.getUuid())
                        .batchId(countBatch)
                        .maxPullPageSize(invocation.getMaxPullPageSize())
                        .maxPushBatchSize(invocation.getMaxPushBatchSize())
                        .productExportEndpoint(invocation.getProductExportEndpoint())
                        .productImportEndpoint(invocation.getProductImportEndpoint())
                        .onCompletionCallbackEndpoint(invocation.getOnCompletionCallbackEndpoint())
                        .build();

                PrepaidOfferEligibilityTrx tx = PrepaidOfferEligibilityTrx.builder()
                        .batchId(Long.valueOf(countBatch))
                        .invocationId(invocation.getUuid())
                        .instanceId(instanceId)
                        .isEvaluated(false)
                        .data(newInvocationPerBatch.getDataSet().toString())
                        .batchSize(newInvocationPerBatch.getDataSet().getSize())
                        .totalRow(invocation.getDataSet().getSize())
                        .build();
                invocation.setBatchId(countBatch);
                prepaidOfferEligibilityTrxRepository.save(tx);
                invocation.setOfferEligibilityTxId(tx.getId());
                processData(newInvocationPerBatch, invocation);
                log.info("========================================END BATCH {}=====================================",
                        countBatch);
                countBatch++;
            }
        } else {
            log.debug("invoke with data");
            PrepaidOfferEligibilityTrx tx = PrepaidOfferEligibilityTrx.builder()
                    .batchId(Long.valueOf(0))
                    .invocationId(invocation.getUuid())
                    .instanceId(instanceId)
                    .isEvaluated(false)
                    .data(invocation.getDataSet().toString())
                    .batchSize(invocation.getDataSet().getSize())
                    .totalRow(invocation.getDataSet().getSize())
                    .build();
            invocation.setBatchId(0);
            prepaidOfferEligibilityTrxRepository.save(tx);
            invocation.setOfferEligibilityTxId(tx.getId());
            processData(invocation, invocation);
        }

        onCompletionCallbackEndpoint(invocation);
    }

    @Override
    public void processData(InvocationRequest invocation, InvocationRequest invocationOri) throws Exception {
        log.debug("call processData");

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
            DataSet dataSet = invocation.getDataSet();

            Instant start = Instant.now();
            log.info("invId|{}|confId|{}|start_process|{}|total_rows|{}", invocation.getUuid(), instanceConfiguration.getId(), start, rows.size());
            sendToEligibilityQueue(dataSet, invocation, invocationOri);
            Duration duration = Duration.between(start, Instant.now());
            log.info("invId|{}|confId|{}|end_process|{}|total_rows|{}", invocation.getUuid(), instanceConfiguration.getId(), TimeUnit.MILLISECONDS.convert(duration), rows.size());

        } catch (Exception e) {
            // TODO: handle exception
            log.error("Exception occurred while processing rows for invocationUuid:" + invocation.getUuid(), e);
        }
    }

    @Override
    public ProductExportEndpointResponse callProductExportEndpoint(InvocationRequest invocation, int limit, int offset) throws Exception {
        log.debug("call productExportEndpoint");

        InstanceContext instanceContext = invocation.getInstanceContext();
        String token = jwtTokenUtil.generateTokenProduct(invocation, instanceContext);
        String url = invocation.getProductImportEndpoint().getUrl() + "?offset=" + offset + "&limit=" + limit;
        ResponseEntity<String> responseEntity = RESTUtil.productExportEndpoint(
                invocation,
                token,
                url,
                null,
                String.class,
                "application/json");

        log.debug(responseEntity.getBody().toString());

        int statusCode = responseEntity.getStatusCodeValue();
        if (statusCode >= 400) {
            String msg = "Calling Product Export Endpoint: " + invocation.getProductExportEndpoint().getUrl()
                    + " resulted in an error status: " + statusCode;
            log.error(msg);
            throw new RuntimeException(msg);
        }

        ProductExportEndpointResponse data = (ProductExportEndpointResponse) GsonUtils.serializeObjectFromJSON(responseEntity.getBody().toString(), ProductExportEndpointResponse.class);
        return data;
    }

    @Override
    public void callProductImportEndpoint(InvocationRequest invocation) throws Exception {
        log.debug("call productImportEndpoint");

        ResponseEntity response = null;
        InstanceContext instanceContext = invocation.getInstanceContext();
        String token = jwtTokenUtil.generateTokenProduct(invocation, instanceContext);
        String url = invocation.getProductImportEndpoint().getUrl();

        List<List<String>> rows = new ArrayList<List<String>>();
        PrepaidCxOfferConfig config =  prepaidCxOfferConfigRepository.findOneByInstanceIdAndDeletedDateIsNull(
          invocation.getInstanceContext().getInstanceId()
        );
        invocation.getDataSet().getRows().forEach(row -> {
            Map<String, Object> input = invocation.getInstanceContext().getRecordDefinition().translateInputRowToMap(row);
            Map<String, Object> output = invocation.getInstanceContext().getRecordDefinition().generateOutputRowAsNewMap(input);
            List<String> listOutput = List.of(
                    output.get("appcloud_row_correlation_id").toString(), //appcloud_row_correlation_id
                    "success", //appcloud_row_status
                    "", //appcloud_row_errormessage
                    config.getOverallOfferName(), //overallOfferName
                    "success"); //STATUS
            rows.add(listOutput);
        });




/*
        {
            "name": "OVERALL_OFFER_NAME",
                "dataType": "Text",
                "width": 200,
                "required": true,
                "readOnly": true
        }
*/

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

    @Override
    public void onCompletionCallbackEndpoint(InvocationRequest invocation) throws Exception {
        BigDecimal batchCount = new BigDecimal(invocation.getDataSet().getSize()).divide(new BigDecimal(batchSize));
        batchCount = batchCount.setScale(0, RoundingMode.UP);
        int count = batchCount.intValue();
        Thread.sleep(count * 5000);
        log.info("{}", invocation.getInstanceContext());
        Thread.sleep(9000);
        log.debug("call onCompletionCallbackEndpoint");

        ResponseEntity response = null;
        InstanceContext instanceContext = invocation.getInstanceContext();
//        String token = jwtTokenUtil.generateTokenProduct(invocation, instanceContext);
        String token = jwtTokenUtil.generateTokenExportProduct(null, instanceContext);
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

    private void sendToEligibilityQueue(DataSet dataSet, InvocationRequest invocation, InvocationRequest invocationOri) {
        InvocationRequest newInvocation = InvocationRequest.builder()
                .uuid(invocation.getUuid())
                .batchId(invocationOri.getBatchId())
                .offerEligibilityTxId(invocationOri.getOfferEligibilityTxId())
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
        map.put("originalInvocationRequest", invocationOri);
        //send to Redemption Queue
        rabbitTemplateConvertAndSendWithPriority(Constant.TOPIC_EXCHANGE_NAME_MEMBERSHIP,
                Constant.QUEUE_NAME_MEMBERSHIP_ELIGIBILITY,
                map,
                0);
    }

}
