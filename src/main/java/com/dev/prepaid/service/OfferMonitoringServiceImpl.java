package com.dev.prepaid.service;

import com.dev.prepaid.constant.Constant;
import com.dev.prepaid.domain.PrepaidCxOfferConfig;
import com.dev.prepaid.domain.PrepaidCxOfferMonitoring;
import com.dev.prepaid.repository.PrepaidCxOfferConfigRepository;
import com.dev.prepaid.repository.PrepaidCxOfferMonitoringRepository;
import com.dev.prepaid.type.EventType;
import com.dev.prepaid.type.OperatorType;
import com.dev.prepaid.type.ProvisionType;
import com.dev.prepaid.type.UsageServiceType;
import com.dev.prepaid.util.GUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class OfferMonitoringServiceImpl implements OfferMonitoringService {

    @Autowired
    PrepaidCxOfferConfigRepository prepaidCxOfferConfigRepository;
    @Autowired
    PrepaidEvaluationService prepaidEvaluationService;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void processUsage(Map<String, Object> payload) {
        String requestId = getRequestId(payload);
        EventType eventType = EventType.USAGE;
        process(requestId, eventType, payload);
    }

    @Override
    public void processTopup(Map<String, Object> payload) {
        String requestId = getRequestId(payload);
        EventType eventType = EventType.TOPUP;
        process(requestId, eventType, payload);
    }

    @Override
    public void processArpu(Map<String, Object> payload) {
        String requestId = getRequestId(payload);
        EventType eventType = EventType.ARPU;
        process(requestId, eventType, payload);
    }

    public void process(String requestId, EventType eventType, Map<String, Object> payload) {
        if (payload.get("instanceId") != null) {
            String instanceId = (String) payload.get("instanceId");
            PrepaidCxOfferConfig prepaidCxOfferConfig = prepaidCxOfferConfigRepository.findOneByInstanceIdAndDeletedDateIsNull(instanceId);
            if(prepaidCxOfferConfig != null) {
                List<String> listOfMsisdn = (List<String>) payload.get("msisdn");
                log.debug("{}|config|{}", requestId, prepaidCxOfferConfig);
                log.debug("{}|msisdnList|{}", requestId, listOfMsisdn);
                sentToQueue(requestId, payload);
                for (String msisdn : listOfMsisdn) {
                    prepaidEvaluationService.evaluationPolicy(
                            msisdn,
                            eventType,
                            prepaidCxOfferConfig,
                            requestId);
                }
            }
        }else{
            for(PrepaidCxOfferConfig prepaidCxOfferConfig: prepaidCxOfferConfigRepository.findAll()){
              if(prepaidCxOfferConfig.getDeletedDate() == null) {
                  List<String> listOfMsisdn = (List<String>) payload.get("msisdn");
                  log.debug("{}|config|{}", requestId, prepaidCxOfferConfig);
                  log.debug("{}|msisdnList|{}", requestId, listOfMsisdn);
                  sentToQueue(requestId, payload);
                  for (String msisdn : listOfMsisdn) {
                      log.debug("{}|msisdn|{}", requestId, msisdn);
                      prepaidEvaluationService.evaluationPolicy(
                              msisdn,
                              eventType,
                              prepaidCxOfferConfig,
                              requestId);
                  }
              }
            }
        }
    }

    private String getRequestId(Map<String, Object> payload) {
        String requestId = null;
        if (payload.get("requestId") == null) {
            requestId = GUIDUtil.generateGUID();
        } else {
            requestId = (String) payload.get("requestId");
        }

        return requestId;
    }

    private void sentToQueue(String requestId, Map<String, Object> payload) {
        boolean sentToQueue = true;
        if(payload.get("sentToQueue") != null){
            sentToQueue = (boolean)  payload.get("sentToQueue");
        }
        if(sentToQueue) {
            log.debug("{}|valid policy monitoring|send to queue|{}|payload|{}", payload.get("requestId"), Constant.QUEUE_NAME_MEMBERSHIP_SUBSCRIBER, payload);
            rabbitTemplate.convertAndSend(Constant.TOPIC_EXCHANGE_NAME_MEMBERSHIP,
                    Constant.QUEUE_NAME_MEMBERSHIP_SUBSCRIBER,
                    payload);
        }else{
            log.debug("{}|skipped|sentToQueue", requestId);
        }
    }

}
