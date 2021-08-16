//package com.dev.prepaid;
//
//import com.dev.prepaid.domain.PrepaidCxOfferConfig;
//import com.dev.prepaid.domain.PrepaidCxOfferEligibility;
//import com.dev.prepaid.domain.PrepaidOfferMembership;
//import com.dev.prepaid.domain.PrepaidOfferMembershipExclus;
//import com.dev.prepaid.model.invocation.DataExportDTO;
//import com.dev.prepaid.model.invocation.InvocationRequest;
//import com.dev.prepaid.repository.*;
//import com.dev.prepaid.service.InvocationService;
//import com.dev.prepaid.service.OfferEligibilityService;
//import com.dev.prepaid.util.GsonUtils;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.Duration;
//import java.time.Instant;
//import java.util.List;
//import java.util.Optional;
//import java.util.concurrent.TimeUnit;
//
//@Slf4j
//@SpringBootTest
//public class EligibilityFunction {
//
//    @Autowired
//    PrepaidCxOfferEligibilityRepository prepaidCxOfferEligibilityRepository;
//    @Autowired
//    PrepaidOfferMembershipRepository prepaidOfferMembershipRepository;
//    @Autowired
//    PrepaidOfferMembershipExclusRepository prepaidOfferMembershipExclusRepository;
//    @Autowired
//    InvocationService invocationService;
//    @Autowired
//    OfferEligibilityService offerEligibilityService;
//    @Autowired
//    PrepaidCxOfferConfigRepository prepaidCxOfferConfigRepository;
//    @Autowired
//    PrepaidCxOfferSelectionRepository prepaidCxOfferSelectionRepository;
//
//    @Test
//    void parsingJson(){
//        ObjectMapper objectMapper = new ObjectMapper();
////		log.debug("try {number1()}");
////		System.out.println("in : "+json);
//        DataExportDTO data = (DataExportDTO) GsonUtils.serializeObjectFromJSON(json, DataExportDTO.class);
//        List<List<String>> rows = data.getDataSet().getRows();
//        System.out.println("rows : "+rows);
//////		DataSet data = (DataSet) GsonUtils.serializeObjectFromJSON(json, DataSet.class);
////		String dataJson = GsonUtils.deserializeObjectToJSON(data);
////		System.out.println("out : "+dataJson);
//    }
//
//    @Test
//    void invoke(){
//        InvocationRequest request = (InvocationRequest) GsonUtils.serializeObjectFromJSON(json, InvocationRequest.class);
//        String instanceId = request.getInstanceContext().getInstanceId();
//        PrepaidCxOfferConfig instanceConfiguration = prepaidCxOfferConfigRepository.findOneByInstanceIdAndDeletedDateIsNull(instanceId);
//        System.out.println(" a " + request.getDataSet());
//        try {
////            invocationService.processData(request);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private static String json ="\t{\n" +
//            "\t  \"dataSet\": {\n" +
//            "\t    \"id\": \"dataSet-id01\",\n" +
//            "\t    \"rows\": [\n" +
//            "\t      [\n" +
//            "\t        \"18e492fd-0c1b-4d6b-b199-62c302139380\",\n" +
//            "\t        \"168125468786\"\n" +
//            "\t      ],\n" +
//            "\t      [\n" +
//            "\t        \"28e492fd-0c1b-4d6b-b199-62c302139380\",\n" +
//            "\t        \"268125468786\"\n" +
//            "\t      ],\n" +
//            "\t      [\n" +
//            "\t        \"38e492fd-0c1b-4d6b-b199-62c302139380\",\n" +
//            "\t        \"368125468786\"\n" +
//            "\t      ],\n" +
//            "\t      [\n" +
//            "\t        \"48e492fd-0c1b-4d6b-b199-62c302139380\",\n" +
//            "\t        \"468125468786\"\n" +
//            "\t      ],\n" +
//            "\t      [\n" +
//            "\t        \"58e492fd-0c1b-4d6b-b199-62c302139380\",\n" +
//            "\t        \"568125468786\"\n" +
//            "\t      ]\n" +
//            "\t    ],\n" +
//            "\t    \"size\": 0\n" +
//            "\t  },\n" +
//            "\t  \"instanceContext\": {\n" +
//            "\t    \"appId\": \"97d16835-90a8-43a6-9a96-dddeacfa9362\",\n" +
//            "\t    \"installId\": \"eb2121ee-7675-4c87-9b0a-a2502892cf8f\",\n" +
//            "\t    \"instanceId\": \"63483226-49f3-48a2-8720-4f4c47021e32\",\n" +
//            "\t    \"serviceId\": \"cc35bf58-049f-48cb-892b-42e1c30f899d\",\n" +
//            "\t    \"secret\": \"string\",\n" +
//            "\t    \"tenantId\": \"string\",\n" +
//            "\t    \"appVersion\": \"string\",\n" +
//            "\t    \"maxBatchSize\": 0,\n" +
//            "\t    \"maxPushBatchSize\": 0,\n" +
//            "\t    \"productId\": \"string\",\n" +
//            "\t    \"recordDefinition\": {\n" +
//            "\t      \"inputParameters\": [\n" +
//            "\t        {\n" +
//            "\t          \"name\": \"appcloud_row_correlation_id\",\n" +
//            "\t          \"dataType\": \"Text\",\n" +
//            "\t          \"width\": 40,\n" +
//            "\t          \"unique\": true,\n" +
//            "\t          \"required\": true,\n" +
//            "\t          \"readOnly\": null,\n" +
//            "\t          \"minimumValue\": null,\n" +
//            "\t          \"maximumValue\": null,\n" +
//            "\t          \"possibleValues\": null,\n" +
//            "\t          \"format\": null,\n" +
//            "\t          \"resources\": null\n" +
//            "\t        },\n" +
//            "\t        {\n" +
//            "\t          \"name\": \"MSISDN\",\n" +
//            "\t          \"dataType\": \"Text\",\n" +
//            "\t          \"width\": 50,\n" +
//            "\t          \"unique\": null,\n" +
//            "\t          \"required\": null,\n" +
//            "\t          \"readOnly\": true,\n" +
//            "\t          \"minimumValue\": null,\n" +
//            "\t          \"maximumValue\": null,\n" +
//            "\t          \"possibleValues\": null,\n" +
//            "\t          \"format\": null,\n" +
//            "\t          \"resources\": null\n" +
//            "\t        }\n" +
//            "\t      ],\n" +
//            "\t      \"outputParameters\": [\n" +
//            "\t        {\n" +
//            "\t          \"name\": \"appcloud_row_correlation_id\",\n" +
//            "\t          \"dataType\": \"Text\",\n" +
//            "\t          \"width\": 40,\n" +
//            "\t          \"unique\": true,\n" +
//            "\t          \"required\": true,\n" +
//            "\t          \"readOnly\": null,\n" +
//            "\t          \"minimumValue\": null,\n" +
//            "\t          \"maximumValue\": null,\n" +
//            "\t          \"possibleValues\": null,\n" +
//            "\t          \"format\": null,\n" +
//            "\t          \"resources\": null\n" +
//            "\t        },\n" +
//            "\t        {\n" +
//            "\t          \"name\": \"appcloud_row_status\",\n" +
//            "\t          \"dataType\": \"Text\",\n" +
//            "\t          \"width\": 10,\n" +
//            "\t          \"unique\": null,\n" +
//            "\t          \"required\": true,\n" +
//            "\t          \"readOnly\": null,\n" +
//            "\t          \"minimumValue\": null,\n" +
//            "\t          \"maximumValue\": null,\n" +
//            "\t          \"possibleValues\": [\n" +
//            "\t            \"success\",\n" +
//            "\t            \"warning\",\n" +
//            "\t            \"failure\"\n" +
//            "\t          ],\n" +
//            "\t          \"format\": null,\n" +
//            "\t          \"resources\": null\n" +
//            "\t        },\n" +
//            "\t        {\n" +
//            "\t          \"name\": \"appcloud_row_errormessage\",\n" +
//            "\t          \"dataType\": \"Text\",\n" +
//            "\t          \"width\": 5120,\n" +
//            "\t          \"unique\": null,\n" +
//            "\t          \"required\": null,\n" +
//            "\t          \"readOnly\": null,\n" +
//            "\t          \"minimumValue\": null,\n" +
//            "\t          \"maximumValue\": null,\n" +
//            "\t          \"possibleValues\": null,\n" +
//            "\t          \"format\": null,\n" +
//            "\t          \"resources\": null\n" +
//            "\t        }\n" +
//            "\t      ]\n" +
//            "\t    }\n" +
//            "\t  },\n" +
//            "\t  \"maxPullPageSize\": 0,\n" +
//            "\t  \"maxPushBatchSize\": 0,\n" +
//            "\t  \"onCompletionCallbackEndpoint\": {\n" +
//            "\t    \"headers\": {},\n" +
//            "\t    \"method\": \"string\",\n" +
//            "\t    \"url\": \"string\"\n" +
//            "\t  },\n" +
//            "\t  \"productExportEndpoint\": {\n" +
//            "\t    \"headers\": {},\n" +
//            "\t    \"method\": \"string\",\n" +
//            "\t    \"url\": \"string\"\n" +
//            "\t  },\n" +
//            "\t  \"productImportEndpoint\": {\n" +
//            "\t    \"headers\": {},\n" +
//            "\t    \"method\": \"string\",\n" +
//            "\t    \"url\": \"string\"\n" +
//            "\t  },\n" +
//            "\t  \"uuid\": \"string\"\n" +
//            "\t}";
//
//
//
//
//    @Test
//    void evaluation(){
//        System.out.println( "====================evaluation=======================");
//        Instant start = Instant.now();
//        Long msisdn = Long.valueOf(10000);
//        String configId = "test";
//        boolean isEligible = true;
//        try {
//            Thread.sleep(1);
//            Iterable<PrepaidCxOfferEligibility> list = prepaidCxOfferEligibilityRepository.findAll();
////            for(PrepaidCxOfferEligibility prepaidCxOfferEligibility : list){
////                System.out.println( "id: " + prepaidCxOfferEligibility.getId());
////            }
//            Optional<PrepaidCxOfferEligibility> opsFind = prepaidCxOfferEligibilityRepository.findById(Long.valueOf(48));
//            if (opsFind.isPresent()) {
//                for (int i = 1; i <= 100; i++){
//                    PrepaidCxOfferEligibility prepaidCxOfferEligibility = opsFind.get();
//                System.out.println("id: " + prepaidCxOfferEligibility.getId());
//                List<PrepaidOfferMembership> current = prepaidOfferMembershipRepository.findByMsisdnAndOfferConfigId(msisdn, configId);
//                if (prepaidCxOfferEligibility.getIsFrequencyOnly()) {
//                    if (current.size() > prepaidCxOfferEligibility.getFrequency()) {
//                        isEligible = false;
//                    } else {
//                        if (prepaidCxOfferEligibility.getIsFrequencyAndTime()) {
//                        }
//                    }
//                } else {
//                    if (prepaidCxOfferEligibility.getIsFrequencyAndTime()) {
//                    }
//                }
//
//                if (prepaidCxOfferEligibility.getIsOfferLevelCapOnly()) {
//
//                } else if (prepaidCxOfferEligibility.getIsOfferLevelCapAndPeriod()) {
//
//                }
//
//                if (isEligible) {
//                    PrepaidOfferMembership prepaidOfferMembership = PrepaidOfferMembership.builder()
//                            .msisdn(msisdn)
//                            .offerConfigId(configId)
//                            .build();
//                    prepaidOfferMembershipRepository.save(prepaidOfferMembership);
//                } else {
//                    PrepaidOfferMembershipExclus prepaidOfferMembershipExclus = PrepaidOfferMembershipExclus.builder()
//                            .msisdn(msisdn)
//                            .offerSelectionId(Long.valueOf(1))
//                            .build();
//                    prepaidOfferMembershipExclusRepository.save(prepaidOfferMembershipExclus);
//                }
//            }
//        }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Instant end = Instant.now();
//        Duration duration = Duration.between(start, end);
//        System.out.println("process time: " + TimeUnit.MILLISECONDS.convert(duration) + " millisecond ");
//        System.out.println( "====================end=======================");
//
//    }
//
//
//
//}
