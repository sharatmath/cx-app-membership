package com.dev.prepaid;

import com.dev.prepaid.domain.*;
import com.dev.prepaid.model.invocation.DataSet;
import com.dev.prepaid.model.invocation.InvocationRequest;
import com.dev.prepaid.repository.*;
import com.dev.prepaid.service.OfferService;
import com.dev.prepaid.util.GUIDUtil;
import com.dev.prepaid.util.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@SpringBootTest
public class EligibilityServiceTest {
    static final String DATE_FORMAT = "yyyyMMdd";
    static final String DATETIME_FORMAT = "yyyyMMdd'|'HHmmss";

    @Autowired
    private OfferService offerService;
    @Autowired
    private PrepaidCxOfferEligibilityRepository prepaidCxOfferEligibilityRepository;
    @Autowired
    private PrepaidCxOfferSelectionRepository prepaidCxOfferSelectionRepository;
    @Autowired
    private PrepaidOfferMembershipRepository prepaidOfferMembershipRepository;
    @Autowired
    private PrepaidOfferMembershipExclusRepository prepaidOfferMembershipExclusRepository;
    @Autowired
    private PrepaidCxProvInvocationsRepository prepaidCxProvInvocationsRepository;
    @Autowired
    private PrepaidCxOfferConfigRepository prepaidCxOfferConfigRepository;
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @Test
    public void testprocessData() {
        Instant start = Instant.now();
        InvocationRequest request = (InvocationRequest) GsonUtils.serializeObjectFromJSON(json, InvocationRequest.class);
        String invocationId = GUIDUtil.generateGUID();
        request.setUuid(invocationId);
        PrepaidCxProvInvocations prepaidCxProvInvocations = PrepaidCxProvInvocations.builder()
                .id(invocationId)
                .instanceId(request.getInstanceContext().getInstanceId())
                .status("ON_PROGRESS")
                .input(GsonUtils.deserializeObjectToJSON(request))
                .output("")
//					.createdBy("prov_invocations")
                .createdDate(new Date())
                .build();
            prepaidCxProvInvocationsRepository.save(prepaidCxProvInvocations);
        String instanceId = request.getInstanceContext().getInstanceId();
        PrepaidCxOfferConfig instanceConfiguration = prepaidCxOfferConfigRepository.findOneByInstanceIdAndDeletedDateIsNull(instanceId);

        DataSet dataSet = generateDataSet(1);
//        DataSet dataSet = request.getDataSet();;
        log.info("start process {} for {} rows ", start, dataSet.getRows().size() );
        processData(dataSet.getRows(), request, instanceConfiguration, null, Long.valueOf(1));

        Duration duration = Duration.between(start, Instant.now());
        log.info("end process {} ms for {} rows ", TimeUnit.MILLISECONDS.convert(duration), dataSet.getRows().size() );
    }

    private List<String> newRow(String id, int msidn){
        List<String> row = new ArrayList<>();
        row.add(id);
        row.add(String.valueOf(msidn));
        return  row;
    }
    private DataSet generateDataSet(int num){
        DataSet d = new DataSet();

        List<List<String>> rows = Stream.iterate(0, n -> n + 1)
                .limit(num)
                .map(x -> {
                    return newRow(String.valueOf(x), x);
                })
                .collect(Collectors.toList());

         d.setRows(rows);

         return  d;
    }

    public void processData(List<List<String>> rows,
                                          InvocationRequest invocation,
                                          PrepaidCxOfferConfig instanceConfiguration,
                                          String groupId,
                                          Long dataSetSize) {
        List<List<String>> eligibleRows = new ArrayList<>();
        List<List<String>> notEligibleRows = new ArrayList<>();
        Optional<PrepaidCxOfferEligibility> prepaidCxOfferEligibilityList = prepaidCxOfferEligibilityRepository.findByOfferConfigId(instanceConfiguration.getId()) ;
        List<PrepaidCxOfferSelection> prepaidCxOfferSelectionList = prepaidCxOfferSelectionRepository.findByOfferConfigId(instanceConfiguration.getId());
        // Process a page of data
        log.info("{}", prepaidCxOfferEligibilityList.get());
        rows.stream()
//        rows.parallelStream()
                .forEach(
                        row -> {
                            Map<String, Object> input = invocation.getInstanceContext().getRecordDefinition().translateInputRowToMap(row);
//                            Map<String, Object> output = invocation.getInstanceContext().getRecordDefinition().generateOutputRowAsNewMap(input);
                            validationSubscriberLevel((String) input.get("MSISDN"), eligibleRows, notEligibleRows, invocation.getUuid(), prepaidCxOfferEligibilityList.get());
                        });
        log.info("invId|{}|confId|{}|END|validationSubscriberLevel|SUMMARY|total_rows|{}|eligible|{}|not_eligible|{}", invocation.getUuid(), instanceConfiguration.getId(), rows.size(), eligibleRows.size(), notEligibleRows.size());
        if(eligibleRows.size() > 0)
            processOfferLevelCap(eligibleRows, prepaidCxOfferEligibilityList.get(), invocation.getUuid(), instanceConfiguration, prepaidCxOfferSelectionList.get(0));
        if(notEligibleRows.size() > 0)
            saveToPrepaidOfferMembershipExclus(notEligibleRows, invocation.getUuid(), instanceConfiguration, prepaidCxOfferSelectionList.get(0));

        //finish
        try {
            PrepaidCxProvInvocations prepaidCxProvInvocations = prepaidCxProvInvocationsRepository.findOneById(invocation.getUuid());
            prepaidCxProvInvocations.setStatus("COMPLETED");

//            if(!instanceConfiguration.getNotification()) {
                prepaidCxProvInvocationsRepository.save(prepaidCxProvInvocations);
//            }

        } catch (Exception e) {
            log.info("ERROR : {}", e.getMessage());
        }

//        return eligibleRows;
    }

    private void validationSubscriberLevel(String msisdn, List<List<String>> eligible, List<List<String>> notEligible, String invId, PrepaidCxOfferEligibility prepaidCxOfferEligibility) {
        if(subscriberLevel(msisdn, prepaidCxOfferEligibility)){
            eligible.add(List.of(msisdn));
            log.info("invId|{}|confId|{}|validationSubscriberLevel|{}|msisdn|{}",invId, prepaidCxOfferEligibility.getOfferConfigId(), true, msisdn);
        }else{
            notEligible.add(List.of(msisdn));
            log.info("invId|{}|confId|{}|validationSubscriberLevel|{}|msisdn|{}",invId, prepaidCxOfferEligibility.getOfferConfigId(), false, msisdn);
        }
    }

    private void processOfferLevelCap(List<List<String>> eligibleRows, PrepaidCxOfferEligibility prepaidCxOfferEligibility, String invId, PrepaidCxOfferConfig prepaidCxOfferConfig, PrepaidCxOfferSelection prepaidCxOfferSelection){
        List<List<String>> offerMembershipRows = new ArrayList<>();
        List<List<String>> offerMembershipExcluseRows = new ArrayList<>();

        if(prepaidCxOfferEligibility.getIsOfferLevelCapOnly()){
            int currentCap = countOfferCapPerOfferConfigId(prepaidCxOfferConfig.getId());
            if(currentCap >=  prepaidCxOfferEligibility.getOfferLevelCapValue()){
                offerMembershipExcluseRows = eligibleRows;
            }else{
                int capacityCap = prepaidCxOfferEligibility.getOfferLevelCapValue().intValue() - currentCap;
                offerMembershipRows = eligibleRows.subList(0, capacityCap);
                offerMembershipExcluseRows = eligibleRows.subList(capacityCap, eligibleRows.size());
            }
        }else if(prepaidCxOfferEligibility.getIsOfferLevelCapAndPeriod()){
            int currentCap = countOfferCapPerOfferConfigIdAndRangePeriod(prepaidCxOfferConfig.getId(), prepaidCxOfferEligibility);
            if(currentCap >= prepaidCxOfferEligibility.getOfferLevelCapPeriodValue()){
                offerMembershipRows = eligibleRows;
            }else{
                int capacityCap = prepaidCxOfferEligibility.getOfferLevelCapPeriodValue().intValue() - currentCap;
                offerMembershipRows = eligibleRows.subList(0, capacityCap);
                offerMembershipExcluseRows = eligibleRows.subList(capacityCap, eligibleRows.size());
            }
        }

        if(offerMembershipRows.size() > 0)
            saveToPrepaidOfferMembership(offerMembershipRows, invId, prepaidCxOfferConfig, prepaidCxOfferSelection);
        if(offerMembershipExcluseRows.size() > 0)
            saveToPrepaidOfferMembershipExclus(offerMembershipExcluseRows, invId, prepaidCxOfferConfig, prepaidCxOfferSelection);
    }

    private Boolean subscriberLevel(String msisdn, PrepaidCxOfferEligibility prepaidCxOfferEligibility) {
        if (prepaidCxOfferEligibility.getIsFrequencyOnly()) {
            int currentFrequency = countFrequencyPerMsisdn(msisdn, prepaidCxOfferEligibility.getOfferConfigId());
            if (currentFrequency >= prepaidCxOfferEligibility.getFrequency()) {
                return false;
            } else {
                if (prepaidCxOfferEligibility.getIsFrequencyAndTime()) {
                    int currentFrequencyInRangeTime = countFrequencyInRangeTime(msisdn, prepaidCxOfferEligibility.getOfferConfigId(), prepaidCxOfferEligibility.getNumberOfDays());
                    if (currentFrequencyInRangeTime > prepaidCxOfferEligibility.getNumberOfFrequency()) {
                        return false;
                    }
                }
            }
        } else if (prepaidCxOfferEligibility.getIsFrequencyAndTime()) {
            int currentFrequencyInRangeTime = countFrequencyInRangeTime(msisdn, prepaidCxOfferEligibility.getOfferConfigId(), prepaidCxOfferEligibility.getNumberOfDays());
            if (currentFrequencyInRangeTime > prepaidCxOfferEligibility.getNumberOfFrequency()) {
                return false;
            }
        }
        return true;
    }

    private int countOfferCapPerOfferConfigId(String offerConfigId){
       return prepaidOfferMembershipRepository.countByOfferConfigId(offerConfigId);
    }

    private int countOfferCapPerOfferConfigIdAndRangePeriod(String offerConfigId, PrepaidCxOfferEligibility prepaidCxOfferEligibility){
        Calendar now = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR, 0);
        start.set(Calendar.MINUTE,0);
        start.set(Calendar.SECOND,0);
        start.add(Calendar.DATE, -prepaidCxOfferEligibility.getNumberOfDays());
        return prepaidOfferMembershipRepository.countByOfferConfigIdAndCreatedDateBetween(offerConfigId, start.getTime(), now.getTime());
    }
    private int countFrequencyPerMsisdn(String msisdn, String offerConfigId){
        List<PrepaidOfferMembership> list = prepaidOfferMembershipRepository.findByMsisdnAndOfferConfigId(Long.valueOf(msisdn), offerConfigId);
        if(!list.isEmpty()){
            return list.size();
        }else {
            return 0;
        }
    }

    private int countFrequencyInRangeTime(String msisdn, String offerConfigId, int rangeDays){
        Calendar now = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.DATE, -rangeDays);

        List<PrepaidOfferMembership> list = prepaidOfferMembershipRepository.findByMsisdnAndOfferConfigIdAndCreatedDateBetween(
                Long.valueOf(msisdn),
                offerConfigId,
                now.getTime(),
                startDate.getTime()
        );

        if(!list.isEmpty()){
            return list.size();
        }else {
            return 0;
        }
    }

    @Test
    public void saveToPrepaidOfferMembership() {
        InvocationRequest request = (InvocationRequest) GsonUtils.serializeObjectFromJSON(json, InvocationRequest.class);
        PrepaidCxOfferConfig prepaidCxOfferConfig = prepaidCxOfferConfigRepository.findOneByInstanceIdAndDeletedDateIsNull(request.getInstanceContext().getInstanceId());
        List<PrepaidCxOfferSelection> prepaidCxOfferSelections = prepaidCxOfferSelectionRepository.findByOfferConfigId(prepaidCxOfferConfig.getId());
        saveToPrepaidOfferMembership(request.getDataSet().getRows(), request.getUuid(), prepaidCxOfferConfig, prepaidCxOfferSelections.get(0));
    }
    private void saveToPrepaidOfferMembership(List<List<String>> membershipRows, String invId, PrepaidCxOfferConfig prepaidCxOfferConfig, PrepaidCxOfferSelection prepaidCxOfferSelection) {
        int totalObjects = membershipRows.size();
        log.info("invId|{}|confId|{}|total|save_to|membership|{}",invId, prepaidCxOfferConfig.getId(), totalObjects);
        List<PrepaidOfferMembership> memberships = membershipRows
                .stream()
                .map(dataRowDTO ->
                        PrepaidOfferMembership.builder()
                                .offerConfigId(prepaidCxOfferConfig.getId())
//                                .offerSelectionId(prepaidCxOfferSelection.getId())
                                .msisdn(Long.valueOf(dataRowDTO.get(1)))
                                .build())
                .collect(Collectors.toList());
        //optimize
        for (int i = 0; i < totalObjects; i += batchSize) {
            if( i+ batchSize > totalObjects){
                List<PrepaidOfferMembership> prepaidOfferMemberships = memberships.subList(i, totalObjects);
                prepaidOfferMembershipRepository.saveAll(prepaidOfferMemberships);
                break;
            }
            List<PrepaidOfferMembership> prepaidOfferMemberships = memberships.subList(i, i + batchSize);
            prepaidOfferMembershipRepository.saveAll(prepaidOfferMemberships);
        }
        //orinal
//        prepaidOfferMembershipRepository.saveAll(memberships);
    }

    @Async
    private void saveToPrepaidOfferMembershipExclus(List<List<String>> membershipExclusRows, String invId, PrepaidCxOfferConfig prepaidCxOfferConfig, PrepaidCxOfferSelection prepaidCxOfferSelection) {
        int totalObjects = membershipExclusRows.size();
        log.info("invId|{}|confId|{}|total|save_to|membership_exclus|{}",invId, prepaidCxOfferConfig.getId(), totalObjects);
        List<PrepaidOfferMembershipExclus> memberships = membershipExclusRows
                .stream()
                .map(dataRowDTO ->
                        PrepaidOfferMembershipExclus.builder()
                                .msisdn(Long.valueOf(dataRowDTO.get(1)))
                                .build())
                .collect(Collectors.toList());
        //optimize
        for (int i = 0; i < totalObjects; i += batchSize) {
            if( i+ batchSize > totalObjects){
                List<PrepaidOfferMembershipExclus> prepaidOfferMembershipExcluses = memberships.subList(i, totalObjects);
                prepaidOfferMembershipExclusRepository.saveAll(prepaidOfferMembershipExcluses);
                break;
            }
            List<PrepaidOfferMembershipExclus> prepaidOfferMembershipExcluses = memberships.subList(i, i + batchSize);
            prepaidOfferMembershipExclusRepository.saveAll(prepaidOfferMembershipExcluses);
        }

        //original
//        prepaidOfferMembershipExclusRepository.saveAll(memberships);
    }

    private static String json ="\t{\n" +
            "\t  \"dataSet\": {\n" +
            "\t    \"id\": \"dataSet-id01\",\n" +
            "\t    \"rows\": [\n" +
            "\t      [\n" +
            "\t        \"18e492fd-0c1b-4d6b-b199-62c302139380\",\n" +
            "\t        \"168125468786\"\n" +
            "\t      ],\n" +
            "\t      [\n" +
            "\t        \"28e492fd-0c1b-4d6b-b199-62c302139380\",\n" +
            "\t        \"268125468786\"\n" +
            "\t      ],\n" +
            "\t      [\n" +
            "\t        \"38e492fd-0c1b-4d6b-b199-62c302139380\",\n" +
            "\t        \"368125468786\"\n" +
            "\t      ],\n" +
            "\t      [\n" +
            "\t        \"48e492fd-0c1b-4d6b-b199-62c302139380\",\n" +
            "\t        \"468125468786\"\n" +
            "\t      ],\n" +
            "\t      [\n" +
            "\t        \"58e492fd-0c1b-4d6b-b199-62c302139380\",\n" +
            "\t        \"568125468786\"\n" +
            "\t      ]\n" +
            "\t    ],\n" +
            "\t    \"size\": 0\n" +
            "\t  },\n" +
            "\t  \"instanceContext\": {\n" +
            "\t    \"appId\": \"97d16835-90a8-43a6-9a96-dddeacfa9362\",\n" +
            "\t    \"installId\": \"eb2121ee-7675-4c87-9b0a-a2502892cf8f\",\n" +
//            "\t    \"instanceId\": \"63483226-49f3-48a2-8720-4f4c47021e32\",\n" +
            "\t    \"instanceId\": \"4420fa19-e8f6-4cdf-9754-d876dea3002f\",\n" +
            "\t    \"serviceId\": \"cc35bf58-049f-48cb-892b-42e1c30f899d\",\n" +
            "\t    \"secret\": \"string\",\n" +
            "\t    \"tenantId\": \"string\",\n" +
            "\t    \"appVersion\": \"string\",\n" +
            "\t    \"maxBatchSize\": 0,\n" +
            "\t    \"maxPushBatchSize\": 0,\n" +
            "\t    \"productId\": \"string\",\n" +
            "\t    \"recordDefinition\": {\n" +
            "\t      \"inputParameters\": [\n" +
            "\t        {\n" +
            "\t          \"name\": \"appcloud_row_correlation_id\",\n" +
            "\t          \"dataType\": \"Text\",\n" +
            "\t          \"width\": 40,\n" +
            "\t          \"unique\": true,\n" +
            "\t          \"required\": true,\n" +
            "\t          \"readOnly\": null,\n" +
            "\t          \"minimumValue\": null,\n" +
            "\t          \"maximumValue\": null,\n" +
            "\t          \"possibleValues\": null,\n" +
            "\t          \"format\": null,\n" +
            "\t          \"resources\": null\n" +
            "\t        },\n" +
            "\t        {\n" +
            "\t          \"name\": \"MSISDN\",\n" +
            "\t          \"dataType\": \"Text\",\n" +
            "\t          \"width\": 50,\n" +
            "\t          \"unique\": null,\n" +
            "\t          \"required\": null,\n" +
            "\t          \"readOnly\": true,\n" +
            "\t          \"minimumValue\": null,\n" +
            "\t          \"maximumValue\": null,\n" +
            "\t          \"possibleValues\": null,\n" +
            "\t          \"format\": null,\n" +
            "\t          \"resources\": null\n" +
            "\t        }\n" +
            "\t      ],\n" +
            "\t      \"outputParameters\": [\n" +
            "\t        {\n" +
            "\t          \"name\": \"appcloud_row_correlation_id\",\n" +
            "\t          \"dataType\": \"Text\",\n" +
            "\t          \"width\": 40,\n" +
            "\t          \"unique\": true,\n" +
            "\t          \"required\": true,\n" +
            "\t          \"readOnly\": null,\n" +
            "\t          \"minimumValue\": null,\n" +
            "\t          \"maximumValue\": null,\n" +
            "\t          \"possibleValues\": null,\n" +
            "\t          \"format\": null,\n" +
            "\t          \"resources\": null\n" +
            "\t        },\n" +
            "\t        {\n" +
            "\t          \"name\": \"appcloud_row_status\",\n" +
            "\t          \"dataType\": \"Text\",\n" +
            "\t          \"width\": 10,\n" +
            "\t          \"unique\": null,\n" +
            "\t          \"required\": true,\n" +
            "\t          \"readOnly\": null,\n" +
            "\t          \"minimumValue\": null,\n" +
            "\t          \"maximumValue\": null,\n" +
            "\t          \"possibleValues\": [\n" +
            "\t            \"success\",\n" +
            "\t            \"warning\",\n" +
            "\t            \"failure\"\n" +
            "\t          ],\n" +
            "\t          \"format\": null,\n" +
            "\t          \"resources\": null\n" +
            "\t        },\n" +
            "\t        {\n" +
            "\t          \"name\": \"appcloud_row_errormessage\",\n" +
            "\t          \"dataType\": \"Text\",\n" +
            "\t          \"width\": 5120,\n" +
            "\t          \"unique\": null,\n" +
            "\t          \"required\": null,\n" +
            "\t          \"readOnly\": null,\n" +
            "\t          \"minimumValue\": null,\n" +
            "\t          \"maximumValue\": null,\n" +
            "\t          \"possibleValues\": null,\n" +
            "\t          \"format\": null,\n" +
            "\t          \"resources\": null\n" +
            "\t        }\n" +
            "\t      ]\n" +
            "\t    }\n" +
            "\t  },\n" +
            "\t  \"maxPullPageSize\": 0,\n" +
            "\t  \"maxPushBatchSize\": 0,\n" +
            "\t  \"onCompletionCallbackEndpoint\": {\n" +
            "\t    \"headers\": {},\n" +
            "\t    \"method\": \"string\",\n" +
            "\t    \"url\": \"string\"\n" +
            "\t  },\n" +
            "\t  \"productExportEndpoint\": {\n" +
            "\t    \"headers\": {},\n" +
            "\t    \"method\": \"string\",\n" +
            "\t    \"url\": \"string\"\n" +
            "\t  },\n" +
            "\t  \"productImportEndpoint\": {\n" +
            "\t    \"headers\": {},\n" +
            "\t    \"method\": \"string\",\n" +
            "\t    \"url\": \"string\"\n" +
            "\t  },\n" +
            "\t  \"uuid\": \"string\"\n" +
            "\t}";
}
