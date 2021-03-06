//package com.dev.prepaid.listener;
//
//import com.dev.prepaid.constant.Constant;
//import com.dev.prepaid.domain.PrepaidCxOfferConfig;
//import com.dev.prepaid.model.invocation.InvocationRequest;
//import com.dev.prepaid.repository.PrepaidCxOfferConfigRepository;
//import com.dev.prepaid.service.OfferEligibilityService;
//import com.dev.prepaid.util.BaseRabbitTemplate;
//import com.dev.prepaid.util.GUIDUtil;
//import com.dev.prepaid.util.GsonUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.json.JSONObject;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.time.Instant;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//@Slf4j
//@Service
//public class AmqpListener extends BaseRabbitTemplate {
//
//    @Autowired
//    OfferEligibilityService offerEligibilityService;
//    @Autowired
//    PrepaidCxOfferConfigRepository prepaidCxOfferConfigRepository;
//
//    private String getRequestId(Map<String, Object> payload) {
//        String requestId = null;
//        if (payload.get("requestId") == null) {
//            requestId = GUIDUtil.generateGUID();
//        } else {
//            requestId = (String) payload.get("requestId");
//        }
//
//        return requestId;
//    }
//
//    @RabbitListener(queues = Constant.QUEUE_NAME_MEMBERSHIP_ELIGIBILITY,
//            containerFactory = Constant.CONNECTION_FACTORY_NAME_MEMBERSHIP_ELIGIBILITY,
//            id = Constant.QUEUE_NAME_MEMBERSHIP_ELIGIBILITY)
//    public void receivedMessageEligibility(final Map<String, Object> request) throws Exception {
//        log.info("{}",request);
//        String source = (String) request.get("source");
//        log.info("{}",source);
//        String batchId = (String) request.get("batchId");
//        log.info("{}",batchId);
//        HashMap<String, Object> map = (HashMap<String, Object>) request.get("invocationRequest");
//        log.info("{}",map);
//        JSONObject jsonObject = new JSONObject(map);
//        log.info("{}",jsonObject);
//        InvocationRequest invocationRequest = (InvocationRequest) GsonUtils.serializeObjectFromJSON(jsonObject.toString(), InvocationRequest.class);
//
//        HashMap<String, Object> mapOri = (HashMap<String, Object>) request.get("originalInvocationRequest");
//        log.info("{}",mapOri);
//        JSONObject jsonObjectOri = new JSONObject(mapOri);
//        log.info("{}",jsonObjectOri);
//        InvocationRequest invocationRequestOri = (InvocationRequest) GsonUtils.serializeObjectFromJSON(jsonObjectOri.toString(), InvocationRequest.class);
//
//        log.info("invoke from queue {}  ori {}",invocationRequest, invocationRequestOri);
//
//        String instanceId = invocationRequest.getInstanceContext().getInstanceId();
//        PrepaidCxOfferConfig instanceConfiguration = prepaidCxOfferConfigRepository.findOneByInstanceIdAndDeletedDateIsNull(instanceId);
//        List<List<String>> rows = invocationRequest.getDataSet().getRows();
//
//        try {
//            Instant start = Instant.now();
//            log.info("invId|{}|confId|{}|start_process|{}|total_rows|{}", invocationRequest.getUuid(), instanceConfiguration.getId(), start, rows.size() );
//            List<List<String>> processedRows = offerEligibilityService.processData(
//                    rows,
//                    invocationRequest,
//                    invocationRequestOri,
//                    instanceConfiguration,
//                    invocationRequest.getDataSet().getId(),
//                    invocationRequest.getDataSet().getSize()
//            );
//            Duration duration = Duration.between(start, Instant.now());
//            log.info("invId|{}|confId|{}|end_process|{}|total_rows|{}",invocationRequest.getUuid(), instanceConfiguration.getId(), TimeUnit.MILLISECONDS.convert(duration), rows.size() );
//
//        } catch (Exception e) {
//            // TODO: handle exception
//            log.error("Exception occurred while processing rows for invocationUuid:" + invocationRequest.getUuid(), e);
//        }
//    }
//
//
//
//}
