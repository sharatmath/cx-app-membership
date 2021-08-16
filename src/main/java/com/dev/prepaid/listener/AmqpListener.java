package com.dev.prepaid.listener;

import java.util.Map;
import com.dev.prepaid.constant.Constant;
import com.dev.prepaid.service.OfferMonitoringService;
import com.dev.prepaid.type.EventType;
import com.dev.prepaid.util.BaseRabbitTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AmqpListener extends BaseRabbitTemplate {

    @Autowired
    OfferMonitoringService offerMonitoringService;

    @RabbitListener(queues = Constant.QUEUE_NAME_MEMBERSHIP_MONITORING,
            containerFactory = "rabbitListenerContainerFactoryForMembership",
            id = Constant.QUEUE_NAME_MEMBERSHIP_MONITORING)
    public void receivedMessage(final Map<String, Object> request) throws Exception {
        log.debug("{}|Event type|{}|Membership data|{}", request.get("requestId"), request.get("evenType"), request);
        EventType eventType = EventType.get((String) request.get("eventType"));
        log.debug("{}|Event type|{}", request.get("requestId"), eventType);
        if(EventType.USAGE.equals(eventType)){
            offerMonitoringService.processUsage(request);
        }else if(EventType.ARPU.equals(eventType)){
            offerMonitoringService.processArpu(request);
        }else if(EventType.TOPUP.equals(eventType)){
            offerMonitoringService.processTopup(request);
        }
    }
}
