package com.dev.prepaid.listener;

import java.util.Map;
import com.dev.prepaid.constant.Constant;
import com.dev.prepaid.model.invocation.InvocationRequest;
import com.dev.prepaid.service.InvocationService;
import com.dev.prepaid.service.OfferEligibilityService;
import com.dev.prepaid.service.OfferMonitoringService;
import com.dev.prepaid.type.EventType;
import com.dev.prepaid.util.BaseRabbitTemplate;
import com.dev.prepaid.util.GUIDUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AmqpListener extends BaseRabbitTemplate {

    @Autowired
    OfferMonitoringService offerMonitoringService;
    @Autowired
    InvocationService invocationService;

    @RabbitListener(queues = Constant.QUEUE_NAME_MEMBERSHIP_MONITORING,
            containerFactory = Constant.CONNECTION_FACTORY_NAME_MEMBERSHIP_MONITORING,
            id = Constant.QUEUE_NAME_MEMBERSHIP_MONITORING)
    public void receivedMessageOfferMonitoring(final Map<String, Object> request) throws Exception {
        String requestId = getRequestId(request);
        log.debug("{}|Event type|{}|Membership data|{}", requestId, request.get("evenType"), request);
        EventType eventType = EventType.get((String) request.get("eventType"));
        log.debug("{}|Event type|{}", requestId, eventType);
        if(EventType.USAGE.equals(eventType)){
            offerMonitoringService.processUsage(request);
        }else if(EventType.ARPU.equals(eventType)){
            offerMonitoringService.processArpu(request);
        }else if(EventType.TOPUP.equals(eventType)){
            offerMonitoringService.processTopup(request);
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

    @RabbitListener(queues = Constant.QUEUE_NAME_MEMBERSHIP_ELIGIBILITY,
            containerFactory = Constant.CONNECTION_FACTORY_NAME_MEMBERSHIP_ELIGIBILITY,
            id = Constant.QUEUE_NAME_MEMBERSHIP_ELIGIBILITY)
    public void receivedMessageEligibility(final Message message) throws Exception {
        log.info("{}",request);
        String source = (String) request.get("source");
        log.info("{}",source);
        InvocationRequest invocationRequest = (InvocationRequest) request.get("invocationRequest");
        log.info("invoke from queue {}",invocationRequest);
        invocationService.processData(invocationRequest);
    }

}
