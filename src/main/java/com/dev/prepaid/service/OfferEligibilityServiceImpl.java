package com.dev.prepaid.service;

import com.dev.prepaid.constant.Constant;
import com.dev.prepaid.domain.*;
import com.dev.prepaid.model.invocation.DataSet;
import com.dev.prepaid.model.invocation.InvocationRequest;
import com.dev.prepaid.repository.*;
import com.dev.prepaid.type.OfferMembershipStatus;
import com.dev.prepaid.type.ProvisionType;
import com.dev.prepaid.util.BaseRabbitTemplate;
import com.dev.prepaid.util.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OfferEligibilityServiceImpl extends BaseRabbitTemplate implements OfferEligibilityService {
    static final String DATE_FORMAT = "yyyyMMdd";
    static final String DATETIME_FORMAT = "yyyyMMdd'|'HHmmss";

    @Value("${eligibility.batch_size:100}")
    private int batchSize;
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
    private PrepaidCxOfferMonitoringRepository prepaidCxOfferMonitoringRepository;
    @Autowired
    private PrepaidCxOfferConfigRepository prepaidCxOfferConfigRepository;
    @Autowired
    private RetryableService retryableService;

    @Override
    public List<List<String>> processData(List<List<String>> rows,
                                          InvocationRequest invocation,
                                          PrepaidCxOfferConfig instanceConfiguration,
                                          String groupId,
                                          Long dataSetSize) throws Exception {

        log.info("startProcess");
        log.info("rows {}", rows);
        //1
        List<List<String>> exclusionRows = new ArrayList<>();
        exclusionRows = evaluationSubscriberExclusion(rows, invocation, instanceConfiguration);
        //2
        List<List<String>> eligibleRows = new ArrayList<>();
        eligibleRows = evaluationSubscriberLevel(exclusionRows, invocation, instanceConfiguration);
        //3
        List<List<String>> advanceFilterRows = new ArrayList<>();
        advanceFilterRows = evaluationAdvanceFilter(eligibleRows, invocation, instanceConfiguration);
        //4
        List<List<String>> offerLevelRows = new ArrayList<>();
        offerLevelRows = evaluationOfferLevelCondition(advanceFilterRows, invocation, instanceConfiguration);
        //5&6
        if (offerLevelRows.size() > 0) {
            List<PrepaidCxOfferSelection> prepaidCxOfferSelectionList = prepaidCxOfferSelectionRepository.findByOfferConfigId(instanceConfiguration.getId());
            saveToPrepaidOfferMembership(offerLevelRows, invocation.getUuid(), instanceConfiguration, prepaidCxOfferSelectionList.get(0));
        }
        //7
        callbackProductComEndpoint(offerLevelRows, invocation, instanceConfiguration);

        //finish
        try {
            PrepaidCxProvInvocations prepaidCxProvInvocations = prepaidCxProvInvocationsRepository.findOneById(invocation.getUuid());
            prepaidCxProvInvocations.setStatus("COMPLETED");
            prepaidCxProvInvocationsRepository.save(prepaidCxProvInvocations);
        } catch (Exception e) {
            log.info("ERROR : {}", e.getMessage());
        }

        return offerLevelRows;
    }

    @Override
    public List<List<String>> evaluationSubscriberExclusion(List<List<String>> rows, InvocationRequest invocation, PrepaidCxOfferConfig instanceConfiguration) throws Exception {
        List<List<String>> resultRows = new ArrayList<>();
        log.info("process#1|evaluationSubscriberExclusion|START|type|{}|id|{}|rows_in|{}",
                instanceConfiguration.getProvisionType(),
                invocation.getUuid(),
                rows.size());

        log.info("process#1|rows|{}", rows);
        Optional<PrepaidCxOfferEligibility> opsFind = prepaidCxOfferEligibilityRepository.findByOfferConfigId(instanceConfiguration.getId());
        String[] excludeOverallOfferName ;
        if(opsFind.isPresent()){
            excludeOverallOfferName = opsFind.get().getExcludeProgramId().split(",");
            for(String overallOfferName : excludeOverallOfferName){
                Optional<PrepaidCxOfferConfig> excludeConfig = prepaidCxOfferConfigRepository.findByOverallOfferName(overallOfferName);
                for(List<String> row : rows){
                    List<PrepaidOfferMembership> data= prepaidOfferMembershipRepository.findByMsisdnAndOfferConfigId(Long.valueOf(row.get(1)), excludeConfig.get().getId());
                    if(data.isEmpty()){
                        resultRows.add(row);
                    }else{
                        log.info("evaluationSubscriberExclusion|{}|INVALID", row.get(1));
                    }
                }
            }
        }

        log.info("process#1|evaluationSubscriberExclusion|END|id|{}|rows_in|{}|rows_out|{}",
                invocation.getUuid(),
                rows.size(),
                resultRows.size());

        return resultRows;
    }

    @Override
    public List<List<String>> evaluationSubscriberLevel(List<List<String>> rows, InvocationRequest invocation, PrepaidCxOfferConfig instanceConfiguration) throws Exception {
        log.info("process#2|START|evaluationSubscriberLevel|type|{}|id|{}|rows_in|{}",
                instanceConfiguration.getProvisionType(),
                invocation.getUuid(),
                rows.size());
        log.info("process#2|rows|{}", rows);
        List<List<String>> eligibleRows = new ArrayList<>();
        List<List<String>> notEligibleRows = new ArrayList<>();
        Optional<PrepaidCxOfferEligibility> prepaidCxOfferEligibilityList = prepaidCxOfferEligibilityRepository.findByOfferConfigId(instanceConfiguration.getId());
        List<PrepaidCxOfferSelection> prepaidCxOfferSelectionList = prepaidCxOfferSelectionRepository.findByOfferConfigId(instanceConfiguration.getId());
        // Process a page of data
        log.info("{}", prepaidCxOfferEligibilityList.get());
        rows.stream()
//        rows.parallelStream()
                .forEach(
                        row -> {
//                            Map<String, Object> input = invocation.getInstanceContext().getRecordDefinition().translateInputRowToMap(row);
//                            Map<String, Object> output = invocation.getInstanceContext().getRecordDefinition().generateOutputRowAsNewMap(input);
//                            validationSubscriberLevel((String) input.get("MSISDN"), eligibleRows, notEligibleRows, invocation.getUuid(), prepaidCxOfferEligibilityList.get());
                            validationSubscriberLevel(row.get(1), eligibleRows, notEligibleRows, invocation.getUuid(), prepaidCxOfferEligibilityList.get());
                        });
        log.info("invId|{}|confId|{}|END|validationSubscriberLevel|SUMMARY|total_rows|{}|eligible|{}|not_eligible|{}", invocation.getUuid(), instanceConfiguration.getId(), rows.size(), eligibleRows.size(), notEligibleRows.size());

        log.info("process#2|END|evaluationSubscriberLevel|id|{}|rows_in|{}|rows_out|{}",
                invocation.getUuid(),
                rows.size(),
                eligibleRows.size());
        return eligibleRows;
    }

    @Override
    public List<List<String>> evaluationAdvanceFilter(List<List<String>> rows, InvocationRequest invocation, PrepaidCxOfferConfig instanceConfiguration) throws Exception {
        List<List<String>> resultRows = new ArrayList<>();
        log.info("process#3|START|evaluationAdvanceFilter|type|{}|id|{}|rows_in|{}",
                instanceConfiguration.getProvisionType(),
                invocation.getUuid(),
                rows.size());
        log.info("process#3|rows|{}", rows);
        log.info("process#3|END|evaluationAdvanceFilter|id|{}|rows_in|{}|rows_out|{}",
                invocation.getUuid(),
                rows.size(),
                resultRows.size());
        return rows;
    }

    @Override
    public List<List<String>> evaluationOfferLevelCondition(List<List<String>> rows, InvocationRequest invocation, PrepaidCxOfferConfig instanceConfiguration) throws Exception {
        log.info("process#4|START|evaluationOfferLevelCondition|type|{}|id|{}|rows_in|{}",
                instanceConfiguration.getProvisionType(),
                invocation.getUuid(),
                rows.size());
        List<List<String>> offerLevelRows = new ArrayList<>();
        List<List<String>> offerMembershipRows = new ArrayList<>();
        List<List<String>> offerMembershipExcluseRows = new ArrayList<>();
        Optional<PrepaidCxOfferEligibility> prepaidCxOfferEligibilityList = prepaidCxOfferEligibilityRepository.findByOfferConfigId(instanceConfiguration.getId());
        List<PrepaidCxOfferSelection> prepaidCxOfferSelectionList = prepaidCxOfferSelectionRepository.findByOfferConfigId(instanceConfiguration.getId());

        if (rows.size() > 0){

            PrepaidCxOfferEligibility prepaidCxOfferEligibility = prepaidCxOfferEligibilityList.get();

            if (prepaidCxOfferEligibility.getIsOfferLevelCapOnly()) {
                int currentCap = countOfferCapPerOfferConfigId(instanceConfiguration.getId());
                log.info("invId|{}|confId|{}|processLevelCapOnly|eligible|{}|currentCap|{}|maximumCapValue|{}",
                        invocation.getUuid(),
                        instanceConfiguration.getId(),
                        rows.size(),
                        currentCap,
                        prepaidCxOfferEligibility.getOfferLevelCapValue()
                );
                if (currentCap >= prepaidCxOfferEligibility.getOfferLevelCapValue()) {
                    offerMembershipExcluseRows = rows;
                } else {
                    int capacityCap = prepaidCxOfferEligibility.getOfferLevelCapValue().intValue() - currentCap;
                    if (rows.size() <= capacityCap) {
                        offerMembershipRows = rows;
                    } else {
                        offerMembershipRows = rows.subList(0, capacityCap);
                        offerMembershipExcluseRows = rows.subList(capacityCap, rows.size());
                    }
                }
            } else if (prepaidCxOfferEligibility.getIsOfferLevelCapAndPeriod()) {
                int currentCap = countOfferCapPerOfferConfigIdAndRangePeriod(instanceConfiguration.getId(), prepaidCxOfferEligibility);
                log.info("invId|{}|confId|{}|processLevelCapAndPeriod|eligible|{}|currentCap|{}|maximumCapValue|{}|in|{}|period_days",
                        invocation.getUuid(),
                        instanceConfiguration.getId(),
                        rows.size(),
                        currentCap,
                        prepaidCxOfferEligibility.getOfferLevelCapPeriodValue(),
                        prepaidCxOfferEligibility.getOfferLevelCapPeriodDays()
                );
                if (currentCap >= prepaidCxOfferEligibility.getOfferLevelCapPeriodValue()) {
                    offerMembershipRows = rows;
                } else {
                    int capacityCap = prepaidCxOfferEligibility.getOfferLevelCapPeriodValue().intValue() - currentCap;
                    offerMembershipRows = rows.subList(0, capacityCap);
                    offerMembershipExcluseRows = rows.subList(capacityCap, rows.size());
                }
            }
            //save to db
            log.info("invId|{}|confId|{}|processLevelCapOnly|eligible|{}|membership|{}|membership_exclus|{}", invocation.getUuid(), instanceConfiguration.getId(), rows.size(), offerMembershipRows.size(), offerMembershipExcluseRows.size());
            if (offerMembershipExcluseRows.size() > 0)
                saveToPrepaidOfferMembershipExclus(offerMembershipExcluseRows, invocation.getUuid(), instanceConfiguration, prepaidCxOfferSelectionList.get(0));
        }

        log.info("process#4|END|evaluationOfferLevelCondition|type|{}|id|{}|rows_in|{}|rows_out|{}",
                instanceConfiguration.getProvisionType(),
                invocation.getUuid(),
                rows.size(),
                offerLevelRows.size()
        );



        return offerMembershipRows;
    }

    @Override
    public void callbackProductComEndpoint(List<List<String>> rows, InvocationRequest invocation, PrepaidCxOfferConfig instanceConfiguration) throws Exception {
        log.info("process#1|callbackProductComEndpoint|START|type|{}|id|{}|rows_in|{}",
                instanceConfiguration.getProvisionType(),
                invocation.getUuid(),
                rows.size());
        DataSet dataSet = DataSet.builder()
                .rows(rows)
                .id(invocation.getDataSet().getId())
                .size(Long.valueOf(rows.size()))
                .build();

        InvocationRequest newInvocationRequest = InvocationRequest.builder()
                .uuid(invocation.getUuid())
                .onCompletionCallbackEndpoint(invocation.getOnCompletionCallbackEndpoint())
                .productExportEndpoint(invocation.getProductExportEndpoint())
                .productImportEndpoint(invocation.getProductImportEndpoint())
                .maxPushBatchSize(invocation.getMaxPushBatchSize())
                .maxPullPageSize(invocation.getMaxPullPageSize())
                .dataSet(dataSet)
                .build();

        retryableService.callProductOnCompletionCallbackEndpoint(invocation);

        log.info("process#1|callbackProductComEndpoint|END|type|{}|id|{}|rows_in|{}",
                instanceConfiguration.getProvisionType(),
                invocation.getUuid(),
                rows.size());
    }

    private void validationSubscriberLevel(String msisdn, List<List<String>> eligible, List<List<String>> notEligible, String invId, PrepaidCxOfferEligibility prepaidCxOfferEligibility) {
        if (subscriberLevel(msisdn, prepaidCxOfferEligibility)) {
            eligible.add(List.of(msisdn));
            log.info("invId|{}|confId|{}|validationSubscriberLevel|{}|msisdn|{}", invId, prepaidCxOfferEligibility.getOfferConfigId(), true, msisdn);
        } else {
            notEligible.add(List.of(msisdn));
            log.info("invId|{}|confId|{}|validationSubscriberLevel|{}|msisdn|{}", invId, prepaidCxOfferEligibility.getOfferConfigId(), false, msisdn);
        }
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

    private int countOfferCapPerOfferConfigId(String offerConfigId) {
        return prepaidOfferMembershipRepository.countByOfferConfigId(offerConfigId);
    }

    private int countOfferCapPerOfferConfigIdAndRangePeriod(String offerConfigId, PrepaidCxOfferEligibility prepaidCxOfferEligibility) {
        Calendar now = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.add(Calendar.DATE, - prepaidCxOfferEligibility.getOfferLevelCapPeriodDays().intValue());
        return prepaidOfferMembershipRepository.countByOfferConfigIdAndCreatedDateBetween(offerConfigId, start.getTime(), now.getTime());
    }

    private int countFrequencyPerMsisdn(String msisdn, String offerConfigId) {
        List<PrepaidOfferMembership> list = prepaidOfferMembershipRepository.findByMsisdnAndOfferConfigId(Long.valueOf(msisdn), offerConfigId);
        if (!list.isEmpty()) {
            return list.size();
        } else {
            return 0;
        }
    }

    private int countFrequencyInRangeTime(String msisdn, String offerConfigId, int rangeDays) {
        Calendar now = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.DATE, -rangeDays);

        List<PrepaidOfferMembership> list = prepaidOfferMembershipRepository.findByMsisdnAndOfferConfigIdAndCreatedDateBetween(
                Long.valueOf(msisdn),
                offerConfigId,
                now.getTime(),
                startDate.getTime()
        );

        if (!list.isEmpty()) {
            return list.size();
        } else {
            return 0;
        }
    }

    private void saveToPrepaidOfferMembership(List<List<String>> membershipRows, String invId, PrepaidCxOfferConfig prepaidCxOfferConfig, PrepaidCxOfferSelection prepaidCxOfferSelection) throws Exception {
        log.info("process#5|START|saveToPrepaidOfferMembership|id|{}|rows_in|",
                invId,
                membershipRows.size());
        int totalObjects = membershipRows.size();
        log.info("invId|{}|confId|{}|total|save_to|membership|{}", invId, prepaidCxOfferConfig.getId(), totalObjects);
        Date offerDate = new Date();
        Date startDate = null;
        Date endDate = null;
        Optional<PrepaidCxOfferMonitoring> opsFind = prepaidCxOfferMonitoringRepository.findByOfferConfigId(prepaidCxOfferConfig.getId());
        if (opsFind.isPresent()) {
            startDate = opsFind.get().getPeriodStartDate();
            endDate = opsFind.get().getPeriodEndDate();
        }
        Date finalStartDate = startDate;
        Date finalEndDate = endDate;
        log.debug("monitoringEndDate-monitoringStartDate {} {}", finalEndDate, finalStartDate);
        List<PrepaidOfferMembership> memberships = membershipRows
                .stream()
                .map(dataRowDTO ->
                        PrepaidOfferMembership.builder()
                                .offerConfigId(prepaidCxOfferConfig.getId())
//                                .offerSelectionId(prepaidCxOfferSelection.getId())
                                .offerDate(offerDate)
                                .monitoringStartDate(finalStartDate)
                                .monitoringEndDate(finalEndDate)
                                .msisdn(Long.valueOf(dataRowDTO.get(0)))
                                .optinFlag("false")
                                .offerMembershipStatus(OfferMembershipStatus.ACTIVE.toString())
                                .build())
                .collect(Collectors.toList());
        //optimize
        for (int i = 0; i < totalObjects; i += batchSize) {
            if (i + batchSize > totalObjects) {
                List<PrepaidOfferMembership> prepaidOfferMemberships = memberships.subList(i, totalObjects);
                List<PrepaidOfferMembership> saved = (List<PrepaidOfferMembership>) prepaidOfferMembershipRepository.saveAll(prepaidOfferMemberships);
                for(PrepaidOfferMembership p: saved){
                    Map<String, Object> map = new HashMap<>();
                    map.put("offerMembershipId", p.getId());
                    map.put("msisdn", p.getMsisdn());
                    map.put("smsKeyword", "");
                    map.put("instanceId", prepaidCxOfferConfig.getInstanceId());
                    sendToRedemptionQueue(invId, map);
                }

                break;
            }
            List<PrepaidOfferMembership> prepaidOfferMemberships = memberships.subList(i, i + batchSize);
            List<PrepaidOfferMembership> saved = (List<PrepaidOfferMembership>) prepaidOfferMembershipRepository.saveAll(prepaidOfferMemberships);
            for(PrepaidOfferMembership p: saved){
                Map<String, Object> map = new HashMap<>();
                map.put("offerMembershipId", p.getId());
                map.put("msisdn", p.getMsisdn());
                map.put("smsKeyword", "");
                map.put("instanceId", prepaidCxOfferConfig.getInstanceId());
                sendToRedemptionQueue(invId, map);
            }
        }
        log.info("process#5|END|saveToPrepaidOfferMembership|id|{}|rows_in|",
                invId,
                membershipRows.size());
        //original
//        prepaidOfferMembershipRepository.saveAll(memberships);
    }

    @Async
    private void saveToPrepaidOfferMembershipExclus(List<List<String>> membershipExclusRows, String invId, PrepaidCxOfferConfig prepaidCxOfferConfig, PrepaidCxOfferSelection prepaidCxOfferSelection) throws Exception {
        int totalObjects = membershipExclusRows.size();
        log.info("invId|{}|confId|{}|total|save_to|membership_exclus|{}", invId, prepaidCxOfferConfig.getId(), totalObjects);
        List<PrepaidOfferMembershipExclus> memberships = membershipExclusRows
                .stream()
                .map(dataRowDTO ->
                        PrepaidOfferMembershipExclus.builder()
                                .offerSelectionId(prepaidCxOfferSelection.getId())
                                .msisdn(Long.valueOf(dataRowDTO.get(0)))
                                .build())
                .collect(Collectors.toList());
        //optimize
        for (int i = 0; i < totalObjects; i += batchSize) {
            if (i + batchSize > totalObjects) {
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


    public ResponseEntity<String> sendToRedemptionQueue(String invId, Map<String, Object> payload) {
        log.info("process#6|START|sendToRedemptionQueue|id|{}|payload|{}",
                invId,
                payload);
        rabbitTemplate.convertAndSend(
                Constant.TOPIC_EXCHANGE_NAME_MEMBERSHIP,
                Constant.QUEUE_NAME_SINGTEL_REDEMPTION,
                payload
        );
        log.info("process#6|END|sendToRedemptionQueue|id|{}",
                invId);
        return ResponseEntity.ok("Success");
    }

}
