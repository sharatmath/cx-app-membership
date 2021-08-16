package com.dev.prepaid.service;

import com.dev.prepaid.constant.Constant;
import com.dev.prepaid.domain.PrepaidCxOfferConfig;
import com.dev.prepaid.domain.PrepaidCxOfferMonitoring;
import com.dev.prepaid.repository.PrepaidCxOfferConfigRepository;
import com.dev.prepaid.repository.PrepaidCxOfferMonitoringRepository;
import com.dev.prepaid.type.OperatorType;
import com.dev.prepaid.type.ProvisionType;
import com.dev.prepaid.type.UsageServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class OfferMonitoringServiceImpl implements OfferMonitoringService{

    @Autowired
    PrepaidCxOfferConfigRepository prepaidCxOfferConfigRepository;
    @Autowired
    PrepaidCxOfferMonitoringRepository prepaidCxOfferMonitoringRepository;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void processUsage(Map<String, Object> payload) {
        String instanceId = (String) payload.get("instanceId");
        PrepaidCxOfferConfig prepaidCxOfferConfig= prepaidCxOfferConfigRepository.findOneByInstanceIdAndDeletedDateIsNull(instanceId);
        log.debug("{}|{}", payload.get("requestId"), prepaidCxOfferConfig);
        if(prepaidCxOfferConfig != null){
            ProvisionType provisionType = ProvisionType.get(prepaidCxOfferConfig.getProvisionType());
            //required for offer monitoring with assignment and offer monitoring only
            if(ProvisionType.OFFER_MONITORING_WITH_OFFER_ASSIGNMENT.equals(provisionType)
                    || ProvisionType.OFFER_MONITROING.equals(provisionType)){
               Optional<PrepaidCxOfferMonitoring> opsFind =  prepaidCxOfferMonitoringRepository.findByOfferConfigId(prepaidCxOfferConfig.getId());
               log.debug("{}|{}", payload.get("requestId"), opsFind);
               if(opsFind.isPresent()){
                   PrepaidCxOfferMonitoring prepaidCxOfferMonitoring = opsFind.get();
                   UsageServiceType usageServiceType = UsageServiceType.get((String) payload.get("UsageServiceType"));
                   Long value = (Long) payload.get("transactionValue");
                   boolean val = evaluate(prepaidCxOfferMonitoring, (String) payload.get("requestId"), value);
                   if(val){
                       sentToQueue(payload);
                   }
               }
            }else{
                sentToQueue(payload);
            }
        }
    }

    @Override
    public void processTopup(Map<String, Object> payload) {
        String instanceId = (String) payload.get("instanceId");
        PrepaidCxOfferConfig prepaidCxOfferConfig= prepaidCxOfferConfigRepository.findOneByInstanceIdAndDeletedDateIsNull(instanceId);
        log.debug("{}|config|{}", payload.get("requestId"), prepaidCxOfferConfig);
        if(prepaidCxOfferConfig != null){
            ProvisionType provisionType = ProvisionType.get(prepaidCxOfferConfig.getProvisionType());
            //required for offer monitoring with assignment and offer monitoring only
            if(ProvisionType.OFFER_MONITORING_WITH_OFFER_ASSIGNMENT.equals(provisionType)
                    || ProvisionType.OFFER_MONITROING.equals(provisionType)){
                Optional<PrepaidCxOfferMonitoring> opsFind =  prepaidCxOfferMonitoringRepository.findByOfferConfigId(prepaidCxOfferConfig.getId());
                log.debug("{}|PrepaidCxOfferMonitoring|{}", payload.get("requestId"), opsFind);
                if(opsFind.isPresent()){
                    PrepaidCxOfferMonitoring prepaidCxOfferMonitoring = opsFind.get();
                    UsageServiceType usageServiceType = UsageServiceType.get((String) payload.get("UsageServiceType"));
                    Long value = (Long) payload.get("transactionValue");
                    boolean val = evaluate(prepaidCxOfferMonitoring, (String) payload.get("requestId"), value);
                    if(val){
                        sentToQueue(payload);
                    }
                }
            }else{
                sentToQueue(payload);
            }
        }
    }

    @Override
    public void processArpu(Map<String, Object> payload) {
        String instanceId = (String) payload.get("instanceId");
        PrepaidCxOfferConfig prepaidCxOfferConfig= prepaidCxOfferConfigRepository.findOneByInstanceIdAndDeletedDateIsNull(instanceId);
        log.debug("{}|config|{}", payload.get("requestId"), prepaidCxOfferConfig);
        if(prepaidCxOfferConfig != null){
            ProvisionType provisionType = ProvisionType.get(prepaidCxOfferConfig.getProvisionType());
            //required for offer monitoring with assignment and offer monitoring only
            if(ProvisionType.OFFER_MONITORING_WITH_OFFER_ASSIGNMENT.equals(provisionType)
                    || ProvisionType.OFFER_MONITROING.equals(provisionType)){
                Optional<PrepaidCxOfferMonitoring> opsFind =  prepaidCxOfferMonitoringRepository.findByOfferConfigId(prepaidCxOfferConfig.getId());
                log.debug("{}|PrepaidCxOfferMonitoring|{}", payload.get("requestId"), opsFind);
                if(opsFind.isPresent()){
                    PrepaidCxOfferMonitoring prepaidCxOfferMonitoring = opsFind.get();
                    UsageServiceType usageServiceType = UsageServiceType.get((String) payload.get("UsageServiceType"));
                    Long value = (Long) payload.get("transactionValue");
                    boolean val = evaluate(prepaidCxOfferMonitoring, (String) payload.get("requestId"), value);
                    if(val){
                        sentToQueue(payload);
                    }
                }
            }else{
                sentToQueue(payload);
            }
        }
    }

    private void sentToQueue(Map<String, Object> payload) {
        log.debug("{}|valid policy monitoring|send to queue|{}|payload|{}",payload.get("requestId"), Constant.QUEUE_NAME_MEMBERSHIP_SUBSCRIBER, payload);
        rabbitTemplate.convertAndSend(Constant.TOPIC_EXCHANGE_NAME_MEMBERSHIP,
                Constant.QUEUE_NAME_MEMBERSHIP_SUBSCRIBER,
                payload);
    }

    private boolean evaluate(PrepaidCxOfferMonitoring prepaidCxOfferMonitoring, String requestId, Long value){
        if(value == null){
            return false;
        }
        OperatorType operatorType = OperatorType.valueOf(prepaidCxOfferMonitoring.getOperatorValue());
        Long configValue = prepaidCxOfferMonitoring.getTransactionValue();
        log.debug("{}|evaluate operator type|{}|value|{}", requestId, operatorType, prepaidCxOfferMonitoring.getOperatorValue());
        if(OperatorType.LESS_THAN.equals(operatorType)){
            if(value < configValue){
                return true;
            }
        }else if(OperatorType.MORE_THAN.equals(operatorType)){
            if(value > configValue){
                return true;
            }
        }else if(OperatorType.EQUAL_TO.equals(operatorType)){
            if(value == configValue){
                return true;
            }
        }else if(OperatorType.LESS_THAN_OR_EQUAL_TO.equals(operatorType)){
            if(value <= configValue){
                return true;
            }
        }else if(operatorType.MORE_THAN_OR_EQUAL_TO.equals(operatorType)){
            if(value >= configValue){
                return true;
            }
        }
        return false;
    }
}
