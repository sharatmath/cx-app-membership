package com.dev.prepaid;

import com.dev.prepaid.constant.Constant;
import com.dev.prepaid.type.EventType;
import com.dev.prepaid.util.GsonUtils;
import jdk.jshell.EvalException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
public class OfferMonitoringTest {

    String payload ="{\n" +
            "  \"instanceId\": \"63483226-49f3-48a2-8720-4f4c47021e32\",\n" +
            "  \"requestId\": \"1\",\n" +
            "  \"eventType\": \"Usage\",\n" +
            "  \"topUpCreditMethod\": null,\n" +
            "  \"topUpProductPackage\": null,\n" +
            "  \"topUpUsageServiceType\": null,\n" +
            "  \"topUpValueOperator\": null,\n" +
            "  \"topUpOperator\": null,\n" +
            "  \"topUpTransactionValue\": null,\n" +
            "  \"topUpCode\": null,\n" +
            "  \"arpuCreditMethod\": null,\n" +
            "  \"arpuProductPackage\": null,\n" +
            "  \"arpuUsageServiceType\": null,\n" +
            "  \"arpuOperatorsValue\": null,\n" +
            "  \"arpuTopUpCode\": null,\n" +
            "  \"arpuOperators\": null,\n" +
            "  \"arpu\": null,\n" +
            "  \"arpuPaidOperators\": null,\n" +
            "  \"arpuPaidTopUp\": null,\n" +
            "  \"usageServiceType\": \"Data\",\n" +
            "  \"usageType\": \"Data Volume\",\n" +
            "  \"usageOperator\": \"Less Than\",\n" +
            "  \"usageValue\": \"10\",\n" +
            "  \"monitorSpecifiedPeriodRadio\": true,\n" +
            "  \"monitorStartDate\": \"2021-08-30T04:37:00.000+00:00\",\n" +
            "  \"monitorEndDate\": \"2021-07-30T04:37:31.000+00:00\",\n" +
            "  \"monitorPeriodRadio\": true,\n" +
            "  \"monitorPeriod\": 0,\n" +
            "  \"monitorPeriodDayMonth\": \"string\"\n" +
            "}";

   @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void test(){
        EventType eventType = EventType.get("Usage");
        if(EventType.USAGE.equals(eventType)){
            log.info("same");
        }
        Map<String, Object> request = (Map<String, Object>) GsonUtils.serializeObjectFromJSON(payload, HashMap.class);
        rabbitTemplate.convertAndSend(
                Constant.TOPIC_EXCHANGE_NAME_MEMBERSHIP,
                Constant.QUEUE_NAME_MEMBERSHIP_MONITORING,
                request);
        log.info("send to {}", Constant.QUEUE_NAME_MEMBERSHIP_MONITORING);
    }

}
