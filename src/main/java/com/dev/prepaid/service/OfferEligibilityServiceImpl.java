package com.dev.prepaid.service;

import com.dev.prepaid.InitData;
import com.dev.prepaid.constant.Constant;
import com.dev.prepaid.domain.*;
import com.dev.prepaid.model.DataRowDTO;
import com.dev.prepaid.model.imports.DataImportDTO;
import com.dev.prepaid.model.invocation.DataSet;
import com.dev.prepaid.model.invocation.InvocationRequest;
import com.dev.prepaid.repository.*;
import com.dev.prepaid.type.OfferMembershipStatus;
import com.dev.prepaid.type.ProvisionType;
import com.dev.prepaid.util.BaseRabbitTemplate;
import com.dev.prepaid.util.JwtTokenUtil;
import com.dev.prepaid.util.RESTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OfferEligibilityServiceImpl extends BaseRabbitTemplate implements OfferEligibilityService {
    static final String DATE_FORMAT = "yyyyMMdd";
    static final String DATETIME_FORMAT = "yyyyMMdd'|'HHmmss";

    @Value("${eligibility.batch_size:100}")
    private int batchSize;
    @Value("${responsys.custom.event.url}")
    private String responsysCustomEventUrl;
    @Value("${responsys.custom.event.folderName}")
    String folderName;
    @Value("${responsys.custom.event.objectName}")
    String objectName;

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
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PrepaidOfferEligibilityTrxRepository prepaidOfferEligibilityTrxRepository;
    @Autowired
    RetryableService retryableService;
    @Autowired
    PrepaidCxOfferAdvanceFilterRepository prepaidCxOfferAdvanceFilterRepository;
    @Autowired
    OfferAdvanceFilterService offerAdvanceFilterService;
    @Autowired
    PrepaidCxOfferEventConditionRepository prepaidCxOfferEventConditionRepository;

    @Override
    public List<List<String>> processData(List<List<String>> rows,
                                          InvocationRequest invocation,
                                          InvocationRequest invocationOri,
                                          PrepaidCxOfferConfig instanceConfiguration,
                                          String groupId,
                                          Long dataSetSize) throws Exception {

        log.info("FindBatchId|Request|{}|{}", invocationOri.getUuid(), invocation.getBatchId());
        //check msisdn
        List<List<String>> newData = new ArrayList<>();
        for(List<String> dt: rows){
            String msisdn = dt.get(1);
            if(msisdn == null | msisdn==""){
                log.info("{}|No CUSTOMER_ID or MSISDN Found|{}", invocationOri.getUuid(), msisdn);
            }else{
                newData.add(dt);
            }
        }

        if(newData.isEmpty() || newData.size() == 0){
            log.info("{}|No Process Data |{}", invocationOri.getUuid(), newData);
            return newData ;
        }
        //1
        List<List<String>> exclusionRows = new ArrayList<>();
        exclusionRows = evaluationSubscriberExclusion(newData, invocation, instanceConfiguration);
        //2
        List<List<String>> eligibleRows = new ArrayList<>();
        eligibleRows = evaluationSubscriberLevel(exclusionRows, invocation, instanceConfiguration);
        //3
        List<List<String>> advanceFilterRows = new ArrayList<>();
        //skip
//        advanceFilterRows = evaluationAdvanceFilter(eligibleRows, invocation, instanceConfiguration);

        //4
        List<List<String>> offerLevelRows = new ArrayList<>();
        offerLevelRows = evaluationOfferLevelCondition(eligibleRows, invocation, instanceConfiguration);
        //5&6
        Optional<PrepaidOfferEligibilityTrx> opsFind = prepaidOfferEligibilityTrxRepository.findByInvocationIdAndBatchId(
                invocation.getUuid(),
                Long.valueOf(invocation.getBatchId())
        );
        log.info("FindBatchId|Response|{}", opsFind);
        if (opsFind.isPresent()) {
            opsFind.get().setIsEvaluated(true);
            prepaidOfferEligibilityTrxRepository.save(opsFind.get());
            log.info("FindBatchId|Update|{}", opsFind.get());
        }

        if (offerLevelRows.size() > 0) {
            if (!ProvisionType.EVENT_CONDITION.getDescription().equals(instanceConfiguration.getProvisionType())) {
                saveToPrepaidOfferMembership(offerLevelRows, invocation.getUuid(), invocation.getOfferEligibilityTxId(), instanceConfiguration);
            } else {
                log.info("Not Save Membership Caused Provision Type {}", instanceConfiguration.getProvisionType());
                Optional<PrepaidCxOfferEventCondition> opsFindEvent = prepaidCxOfferEventConditionRepository.findByOfferConfigId(instanceConfiguration.getId());
                if(opsFindEvent.isPresent()) {
                    if(opsFindEvent.get().getEventConditionName() != null) {
                        log.info("sendToCustomEventQueue");
                        sendToCustomEventQueue(invocationOri.getUuid(), opsFindEvent.get().getEventConditionName(), offerLevelRows);
                    }else{
                        log.info("no sendToCustomEventQueue caused event condition name {} ", opsFindEvent.get().getEventConditionName() );
                    }
                }else{
                    log.info("no sendToCustomEventQueue cause no found config {} ", instanceConfiguration.getId());
                }
            }
        }
        //7
        if (ProvisionType.DIRECT_PROVISION.getDescription().equals(instanceConfiguration.getProvisionType()) ||
                ProvisionType.OFFER_MONITORING_WITH_OFFER_ASSIGNMENT.getDescription().equals(instanceConfiguration.getProvisionType())) {
            productImportEndpoint(offerLevelRows, invocation, invocationOri, instanceConfiguration);
            try {
                PrepaidCxProvInvocations prepaidCxProvInvocations = prepaidCxProvInvocationsRepository.findOneById(invocation.getUuid());
                prepaidCxProvInvocations.setStatus("COMPLETED");
                prepaidCxProvInvocationsRepository.save(prepaidCxProvInvocations);
            } catch (Exception e) {
                log.error("ERROR : {}", e);
            }
        }

        return offerLevelRows;
    }

    @Override
    public List<List<String>> evaluationSubscriberExclusion(List<List<String>> rows, InvocationRequest invocation, PrepaidCxOfferConfig instanceConfiguration) throws Exception {
        List<List<String>> resultRows = new ArrayList<>();
        List<List<String>> excluseRows = new ArrayList<>();
        log.info("process#1|evaluationSubscriberExclusion|START|type|{}", instanceConfiguration.getProvisionType());
        log.info("process#1|DATA|{}", rows);
        Optional<PrepaidCxOfferEligibility> opsFind = prepaidCxOfferEligibilityRepository.findByOfferConfigId(instanceConfiguration.getId());
        log.info("process#1|{}|config|{}", opsFind, instanceConfiguration.getId());

        if (!opsFind.isPresent()) {
            log.info("process#1|SUMMARY_IN|{}|rows|{}", rows.size(), rows);
            log.info("process#1|SUMMARY_OUT|{}|rows|{}", rows.size(), rows);
            log.info("process#1|evaluationSubscriberExclusion|END");
            return rows;
        }

        String[] excludeOverallOfferName = null;
        String getExcludeProgramId = opsFind.get().getExcludeProgramId();
        log.info("process#1|excludeOverallOfferName|{}|", getExcludeProgramId);
        for (List<String> row : rows) {
            if (getExcludeProgramId == null || getExcludeProgramId == "" || getExcludeProgramId == "null") {
                log.info("process#1|3|EXCLUSION|{}|PASS", row.get(1));
                resultRows.add(row);
            } else {
                excludeOverallOfferName = opsFind.get().getExcludeProgramId().split(",");

                for (String overallOfferName : excludeOverallOfferName) {
                    boolean checkIsExist = false;
                    Optional<PrepaidCxOfferConfig> excludeConfig = prepaidCxOfferConfigRepository.findByOverallOfferName(overallOfferName);
                    if (excludeConfig.isPresent()) {
                        List<PrepaidOfferMembership> data = prepaidOfferMembershipRepository.findByMsisdnAndOfferConfigId(
                                Long.valueOf(row.get(1)),
                                excludeConfig.get().getId()
                        );
                        if (data.isEmpty()) {
                        } else {
                            checkIsExist = true;
                        }
                    } else {
                        log.info("process#1|2|EXCLUSION|{}|PASS", row.get(1));
                        resultRows.add(row);
                    }

                    if (!checkIsExist) {
                        log.info("process#1|1|EXCLUSION|{}|PASS", row.get(1));
                        resultRows.add(row);
                    } else {
                        log.info("process#1|1|EXCLUSION|{}|NOT_PASS", row.get(1));
                        excluseRows.add(row);
                    }

                }



            }
        }
        log.info("process#1|SUMMARY_IN|{}", rows.size());
        log.info("process#1|SUMMARY_OUT|{}|rows|{}", resultRows.size(), resultRows);
        log.info("process#1|SUMMARY_EXCLUSE|{}|rows|{}", excluseRows.size(), excluseRows);
        if (excluseRows.size() > 0) {
            saveToPrepaidOfferMembershipExclus(excluseRows, invocation.getUuid(), invocation.getOfferEligibilityTxId(), instanceConfiguration,
                    "evaluationSubscriberExclusion",
                    "FAILED"
            );
        }
        log.info("process#1|evaluationSubscriberExclusion|END");
        return resultRows;
    }

    @Override
    public List<List<String>> evaluationSubscriberLevel(List<List<String>> rows, InvocationRequest invocation, PrepaidCxOfferConfig instanceConfiguration) throws Exception {
        log.info("process#2|evaluationSubscriberLevel|START|type|{}", instanceConfiguration.getProvisionType());
        log.info("process#2|DATA|{}", rows);
        List<List<String>> eligibleRows = new ArrayList<>();
        List<List<String>> notEligibleRows = new ArrayList<>();
        Optional<PrepaidCxOfferEligibility> prepaidCxOfferEligibilityList = prepaidCxOfferEligibilityRepository.findByOfferConfigId(instanceConfiguration.getId());
        if (!prepaidCxOfferEligibilityList.isPresent()) {
            log.info("process#2|evaluationSubscriberLevel|SKIP|eligibility not found");
            return rows;
        }
        List<PrepaidCxOfferSelection> prepaidCxOfferSelectionList = prepaidCxOfferSelectionRepository.findByOfferConfigId(instanceConfiguration.getId());
        // Process a page of data
        PrepaidCxOfferEligibility e = prepaidCxOfferEligibilityList.get();
        log.info("process#2|OFFER_CONFIG_ID|{}", e.getOfferConfigId());
        if (e.getIsFrequencyAndTime() == null && e.getIsFrequencyOnly() == null) {
            log.info("process#2|SKIP|evaluationSubscriberLevel|getIsFrequencyOnly|{}|getIsFrequencyAndTime|{}", e.getIsFrequencyOnly(), e.getIsFrequencyAndTime());
            return rows;
        }

        boolean isFrequencyOnly = false;
        if(e.getIsFrequencyOnly() == null){
            
        }else{
            isFrequencyOnly = e.getIsFrequencyOnly();
        }

        boolean isFrequencyAndTime = false;
        if(e.getIsFrequencyAndTime() == null){

        }else{
            isFrequencyAndTime = e.getIsFrequencyAndTime();
        }

        log.info("process#2|IS_FREQUENCY_ONLY|{}|VALUE|{}", e.getIsFrequencyOnly(), e.getFrequency());
        log.info("process#2|IS_FREQUENCY_AND_TIME|{}|VALUE|{} IN {} Days", e.getIsFrequencyAndTime(), e.getNumberOfFrequency(), e.getNumberOfDays());
       
        Map<String, Integer> currentCap = new HashMap<String, Integer>();
        for (List<String> row : rows) {  
            if (subscriberLevel(currentCap, row.get(1), e, isFrequencyOnly, isFrequencyAndTime)) {
                if(isFrequencyOnly){
                    if(currentCap.get(row.get(1) + "countFrequencyPerMsisdn") != null) {
                        Integer cap = currentCap.get(row.get(1) + "countFrequencyPerMsisdn");
                        currentCap.put(row.get(1) + "countFrequencyPerMsisdn", cap + 1);
                    }else{
                        currentCap.put(row.get(1) + "countFrequencyPerMsisdn", 1);
                    }
                }
                if(isFrequencyAndTime){
                    if(currentCap.get(row.get(1) + "currentFrequencyInRangeTime") != null){
                        Integer cap = currentCap.get(row.get(1) + "currentFrequencyInRangeTime");
                        currentCap.put(row.get(1) + "currentFrequencyInRangeTime", cap + 1);
                    }else{
                        currentCap.put(row.get(1) + "currentFrequencyInRangeTime",  1);
                    }

                }
                eligibleRows.add(row);            
            } else {
                log.info("process#2|SUBSCRIBER_LEVEL|msisdn|{}|NOT_PASS", row.get(1));
                notEligibleRows.add(row);
            }
        }
        log.info("process#2|SUMMARY_IN|{}|", rows.size());
        log.info("process#2|SUMMARY_OUT|{}|rows|{}", eligibleRows.size(), eligibleRows);
        log.info("process#2|SUMMARY_EXCLUSE|{}|rows|{}", notEligibleRows.size(), notEligibleRows);
        if (notEligibleRows.size() > 0) {
            saveToPrepaidOfferMembershipExclus(notEligibleRows, invocation.getUuid(), invocation.getOfferEligibilityTxId(), instanceConfiguration,
                    "evaluationSubscriberLevel",
                    "FAILED");
        }
        log.info("process#2|evaluationSubscriberLevel|END");
        return eligibleRows;
    }

    @Override
    public List<List<String>> evaluationAdvanceFilter(List<List<String>> rows, InvocationRequest invocation, PrepaidCxOfferConfig instanceConfiguration) throws Exception {
        log.info("process#3|evaluationAdvanceFilter|START|type|{}", instanceConfiguration.getProvisionType());
        log.info("process#3|DATA|{}", rows);
        // getConfigAdvanceFilterQuery with parameter offerConfigId
        Optional<PrepaidCxOfferAdvanceFilter> opsAdv = prepaidCxOfferAdvanceFilterRepository.findByOfferConfigId(instanceConfiguration.getId());


        if (!opsAdv.isPresent()) {
            log.info("process#3|SKIP|{}", rows);
            return rows;
        }
        PrepaidCxOfferAdvanceFilter prepaidCxOfferAdvanceFilter = opsAdv.get();
        log.info("process#3|Execute|{}", prepaidCxOfferAdvanceFilter.getQueryText());
        // getMsisdn from query
        List<String> msisdnList = offerAdvanceFilterService.queryMsisdnByAdvanceFilter(invocation.getUuid(), prepaidCxOfferAdvanceFilter.getQueryText());
        log.info("process#3|Execute|Result|{}", msisdnList);
        List<List<String>> advanceRows = new ArrayList<>();
        List<List<String>> advanceExcluseRows = new ArrayList<>();
        if(msisdnList.isEmpty() || msisdnList.size() == 0 ){
            advanceRows.addAll(rows);
        }
        else {
            // compare msisdn eligible with msisdn from advance filter query
            for (List<String> r : rows) {
                String msisdn = r.get(1);
                if (msisdnList.contains(msisdn)) {
                    advanceRows.add(r);
                } else {
                    advanceExcluseRows.add(r);
                }
            }
        }
        log.info("process#3|SUMMARY_IN|{}|", rows.size());
        log.info("process#3|SUMMARY_OUT|{}|rows|{}", advanceRows.size(), advanceExcluseRows);
        log.info("process#3|SUMMARY_EXCLUSE|{}|rows|{}", advanceExcluseRows.size(), advanceExcluseRows);
        if (advanceExcluseRows.size() > 0) {
            saveToPrepaidOfferMembershipExclus(advanceExcluseRows, invocation.getUuid(), invocation.getOfferEligibilityTxId(), instanceConfiguration,
                    "evaluationAdvanceFilter",
                    "FAILED");
        }
        log.info("process#3|evaluationAdvanceFilter|END");
        // match msisdn put in queue redemption
        return advanceRows;
    }

    @Override
    public List<List<String>> evaluationOfferLevelCondition(List<List<String>> rows, InvocationRequest invocation, PrepaidCxOfferConfig instanceConfiguration) throws Exception {
        log.info("process#4|evaluationOfferLevelCondition|START|type|{}", instanceConfiguration.getProvisionType());
        log.info("process#4|DATA|{}", rows);
        List<List<String>> offerMembershipRows = new ArrayList<>();
        List<List<String>> offerMembershipExcluseRows = new ArrayList<>();
        Optional<PrepaidCxOfferEligibility> prepaidCxOfferEligibilityList = prepaidCxOfferEligibilityRepository.findByOfferConfigId(instanceConfiguration.getId());
        if (!prepaidCxOfferEligibilityList.isPresent()) {
            log.info("process#4|evaluationOfferLevelCondition|Skip|");
            return rows;
        }
        log.info("process#4|{}|config|{}", prepaidCxOfferEligibilityList, instanceConfiguration.getId());
        PrepaidCxOfferEligibility prepaidCxOfferEligibility = prepaidCxOfferEligibilityList.get();
        log.info("process#4|OFFER_CONFIG_ID|{}", prepaidCxOfferEligibility.getOfferConfigId());
        log.info("process#4|IS_OFFER_LEVEL_CAP_ONLY|{}|VALUE|{}",
                prepaidCxOfferEligibility.getIsOfferLevelCapOnly(),
                prepaidCxOfferEligibility.getOfferLevelCapValue()
        );

        log.info("process#4|IS_OFFER_LEVEL_CAP_AND_PERIOD|{}|VALUE|{} IN {} Days",
                prepaidCxOfferEligibility.getIsOfferLevelCapAndPeriod()
                , prepaidCxOfferEligibility.getOfferLevelCapPeriodValue()
                , prepaidCxOfferEligibility.getOfferLevelCapPeriodDays()
        );

        boolean isOfferLevelCapOnly = false;
        boolean isOfferLevelCapAndPeriod = false;
        if (prepaidCxOfferEligibility.getIsOfferLevelCapOnly() != null) {
            isOfferLevelCapOnly = prepaidCxOfferEligibility.getIsOfferLevelCapOnly();
        }

        if (prepaidCxOfferEligibility.getIsOfferLevelCapAndPeriod() != null) {
            isOfferLevelCapAndPeriod = prepaidCxOfferEligibility.getIsOfferLevelCapAndPeriod();
        }

        log.info("process#4|IS_OFFER_LEVEL_CAP|{}|VALUE|{}",
                isOfferLevelCapOnly,
                isOfferLevelCapAndPeriod
        );


        if (rows.size() > 0) {
            if (isOfferLevelCapOnly) {
                int currentCap = countOfferCapPerOfferConfigId(instanceConfiguration.getId());
                log.info("process#4|eligible|{}|currentCap|{}|maximumCapValue|{}",
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
            } else if (isOfferLevelCapAndPeriod) {
                int currentCap = countOfferCapPerOfferConfigIdAndRangePeriod(instanceConfiguration.getId(), prepaidCxOfferEligibility);
                log.info("process#4|eligible|{}|currentCap|{}|maximumCapValue|{}|in|{}|period_days",
                        rows.size(),
                        currentCap,
                        prepaidCxOfferEligibility.getOfferLevelCapPeriodValue(),
                        prepaidCxOfferEligibility.getOfferLevelCapPeriodDays()
                );
                if (currentCap >= prepaidCxOfferEligibility.getOfferLevelCapPeriodValue()) {
                    offerMembershipExcluseRows = rows;
                } else {
                    int capacityCap = prepaidCxOfferEligibility.getOfferLevelCapPeriodValue().intValue() - currentCap;
                    int total_row = rows.size();
                    if(total_row>capacityCap) {
                       // do noting
                    }else{
                        capacityCap = total_row;
                    }
                    offerMembershipRows = rows.subList(0, capacityCap);
                    offerMembershipExcluseRows = rows.subList(capacityCap, rows.size());
                }
            } else {
                offerMembershipRows = rows;
            }

            if (offerMembershipExcluseRows.size() > 0)
                saveToPrepaidOfferMembershipExclus(offerMembershipExcluseRows, invocation.getUuid(), invocation.getOfferEligibilityTxId(), instanceConfiguration,
                        "evaluationOfferLevelCondition",
                        "FAILED"
                );
        }
        log.info("process#4|SUMMARY_IN|{}|", rows.size());
        log.info("process#4|SUMMARY_OUT|{}|rows|{}", offerMembershipRows.size(), offerMembershipRows);
        log.info("process#4|SUMMARY_EXCLUSE|{}|rows|{}", offerMembershipExcluseRows.size(), offerMembershipExcluseRows);
        log.info("process#4|evaluationOfferLevelCondition|END");
        return offerMembershipRows;
    }

    @Override
    public void productImportEndpoint(List<List<String>> rows, InvocationRequest invocation, InvocationRequest invocationOri, PrepaidCxOfferConfig instanceConfiguration) throws Exception {
        log.info("process#7|productImportEndpoint|START|type|{}|id|{}|rows_in|{}",
                instanceConfiguration.getProvisionType(),
                invocation.getUuid(),
                rows.size());
        List<List<String>> newRowOuput = new ArrayList<>();

//
//        InvocationRequest newInvocationRequest = InvocationRequest.builder()
//                .uuid(invocation.getUuid())
//                .instanceContext(invocation.getInstanceContext())
//                .onCompletionCallbackEndpoint(invocation.getOnCompletionCallbackEndpoint())
//                .productExportEndpoint(invocation.getProductExportEndpoint())
//                .productImportEndpoint(invocation.getProductImportEndpoint())
//                .maxPushBatchSize(invocation.getMaxPushBatchSize())
//                .maxPullPageSize(invocation.getMaxPullPageSize())
//                .dataSet(dataSet)
//                .build();


        ResponseEntity response = null;
        log.info("process#7|generateTokenProduct {}", invocationOri);
        String token = jwtTokenUtil.generateTokenProduct(invocationOri, invocationOri.getInstanceContext());
        String url = invocation.getProductImportEndpoint().getUrl();
        List<List<String>> dataRows = invocation.getDataSet().getRows();
        log.info("process#7|dataRows|{}", dataRows);
        int total_row = invocation.getDataSet().getRows().size();
        DataSet dataSetTemp = invocation.getDataSet();
        for (int i = 0; i < total_row; i++) {
            List<String> row = dataSetTemp.getRows().get(i);
            log.info("process#7|productImportEndpoint|index|{}|row|{}", i, row);
            Map<String, Object> input = invocation.getInstanceContext().getRecordDefinition().translateInputRowToMap(row);
            log.info("process#7|productImportEndpoint|index|{}|input|{}", i, input);
            Map<String, Object> output = invocation.getInstanceContext().getRecordDefinition().generateOutputRowAsNewMap(input);
            log.info("process#7|productImportEndpoint|index|{}|output|{}", i, output);
            log.info("process#7|productImportEndpoint|instanceConfiguration|{}|", instanceConfiguration);
            List<String> listOutput = new ArrayList<>();
            listOutput.add(0, output.get("appcloud_row_correlation_id").toString());
            listOutput.add(1, "success");
            listOutput.add(2, "");
            listOutput.add(3, instanceConfiguration.getOverallOfferName());
            listOutput.add(4, "success");
//            List<String> listOutput = List.of(
//                    output.get("appcloud_row_correlation_id").toString(), //appcloud_row_correlation_id
//                    "success", //appcloud_row_status
//                    "", //appcloud_row_errormessage
//                    instanceConfiguration.getOverallOfferName(),
//                    "success"); //STATUS
//            rows.add(listOutput);
            newRowOuput.add(listOutput);
        }

        DataSet dataSet = DataSet.builder()
//                .rows(rows)
                .rows(newRowOuput)
                .id(invocation.getDataSet().getId())
                .size(Long.valueOf(newRowOuput.size()))
                .build();

        DataImportDTO data = DataImportDTO.builder()
                .fieldDefinitions(InitData.recordDefinition.getOutputParameters())
                .dataSet(dataSet)
                .build();
        try {
            log.debug("process#7|productImportPost data output : {}", data);
            response = RESTUtil.productImportPost(invocationOri, token, url, data, null, "application/json");
            log.debug("process#7|productImportPost response : {}", response.getStatusCode());
        } catch (Exception ex) {
            log.error("productImportPost FAILED and INIT RETRY IN 3 times", ex);
            retryableService.callProductImportEndpoint(invocation);
        }

        log.info("process#7|productImportEndpoint|END|type|{}|id|{}|rows_in|{}",
                instanceConfiguration.getProvisionType(),
                invocation.getUuid(),
                rows.size());
    }

    private Boolean subscriberLevel(Map<String, Integer> mapCurrentCap, String msisdn, PrepaidCxOfferEligibility prepaidCxOfferEligibility, boolean isFrequencyOnly, boolean isFrequencyAndTime) {
        log.info("process#2|subscriberLevel|{}|VALUE|{}", msisdn, prepaidCxOfferEligibility);
        if (isFrequencyOnly) {
            int currentFrequency = countFrequencyPerMsisdn(msisdn, prepaidCxOfferEligibility.getOfferConfigId());
            if(mapCurrentCap.get(msisdn + "countFrequencyPerMsisdn") != null) {
                currentFrequency = currentFrequency + mapCurrentCap.get(msisdn + "countFrequencyPerMsisdn");
            }
            log.info("process#2|IsFrequencyOnly|{}|VS|{}", currentFrequency, prepaidCxOfferEligibility.getFrequency() );
            if (currentFrequency >= prepaidCxOfferEligibility.getFrequency()) {
                log.info("process#2|IsFrequencyOnly|{}|Result|{}", msisdn, false);
                return false;
            } else {
                if (isFrequencyAndTime) {
                    int currentFrequencyInRangeTime = countFrequencyInRangeTime(msisdn, prepaidCxOfferEligibility.getOfferConfigId(), prepaidCxOfferEligibility.getNumberOfDays());
                    if(mapCurrentCap.get(msisdn + "currentFrequencyInRangeTime") != null) {
                        currentFrequencyInRangeTime = currentFrequencyInRangeTime + mapCurrentCap.get(msisdn + "currentFrequencyInRangeTime");
                    }
                    log.info("process#2|isFrequencyAndTime|{}|VS|{}", currentFrequencyInRangeTime, prepaidCxOfferEligibility.getNumberOfFrequency() );
                    if (currentFrequencyInRangeTime > prepaidCxOfferEligibility.getNumberOfFrequency()) {
                        log.info("process#2|isFrequencyAndTime|{}|Result|{}", msisdn, false);
                        return false;
                    }
                }
            }
        } else if (isFrequencyAndTime) {
            int currentFrequencyInRangeTime = countFrequencyInRangeTime(msisdn, prepaidCxOfferEligibility.getOfferConfigId(), prepaidCxOfferEligibility.getNumberOfDays());
            if(mapCurrentCap.get(msisdn + "currentFrequencyInRangeTime") != null) {
                currentFrequencyInRangeTime = currentFrequencyInRangeTime + mapCurrentCap.get(msisdn + "currentFrequencyInRangeTime");
            }
            log.info("process#2|isFrequencyAndTime|{}|VS|{}", currentFrequencyInRangeTime, prepaidCxOfferEligibility.getNumberOfFrequency() );
            if (currentFrequencyInRangeTime > prepaidCxOfferEligibility.getNumberOfFrequency()) {
                log.info("process#2|isFrequencyAndTime|{}|Result|{}", msisdn, false);
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
        //start.set(Calendar.HOUR, 0);
        //start.set(Calendar.MINUTE, 0);
        //start.set(Calendar.SECOND, 0);
        start.add(Calendar.HOUR, -1 * 24 * prepaidCxOfferEligibility.getOfferLevelCapPeriodDays().intValue());
        //start.add(Calendar.DATE, -prepaidCxOfferEligibility.getOfferLevelCapPeriodDays().intValue());
        log.info("process#4|countOfferCapPerOfferConfigIdAndRangePeriod|configId|{}|startDate|{}|endDate|{}", offerConfigId, start.getTime(), now.getTime());
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

    private void saveToPrepaidOfferMembership(List<List<String>> membershipRows, String invId, Long offerEligibilityTxId, PrepaidCxOfferConfig prepaidCxOfferConfig) throws Exception {
        log.info("process#5|START|saveToPrepaidOfferMembership");
        log.info("process#5|DATA|{}", membershipRows.size());
        int totalObjects = membershipRows.size();
        Date offerDate = new Date();
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        Optional<PrepaidCxOfferMonitoring> opsFind = prepaidCxOfferMonitoringRepository.findByOfferConfigId(prepaidCxOfferConfig.getId());
        log.info("opsFind {}", opsFind);
        if (opsFind.isPresent()) {
            PrepaidCxOfferMonitoring prepaidCxOfferMonitoring = opsFind.get();
            log.info("PrepaidCxOfferMonitoring {}", prepaidCxOfferMonitoring);
            boolean monitorSpecificPeriod = false;
            if(prepaidCxOfferMonitoring.getIsMonitorSpecificPeriod() != null){
                monitorSpecificPeriod = prepaidCxOfferMonitoring.getIsMonitorSpecificPeriod();
            }
            boolean monitorPeriod = false;
            if(prepaidCxOfferMonitoring.getIsMonitorDateRange() != null) {
                monitorPeriod = prepaidCxOfferMonitoring.getIsMonitorDateRange();
            }
            log.info("process#5|monitorSpecificPeriod-monitorPeriod {} {}", monitorSpecificPeriod, monitorPeriod);
            if(monitorSpecificPeriod) {
                log.info("process#5|monitorSpecificPeriod {} ", monitorSpecificPeriod);
                    startDate = opsFind.get().getPeriodStartDate();
                    endDate = opsFind.get().getPeriodEndDate();
            }else if(monitorPeriod){
                log.info("process#5|monitorPeriod {} ", monitorPeriod);
                int rangeTime = opsFind.get().getPeriodDays();
                String rangeType = opsFind.get().getPeriod();
                startDate = LocalDateTime.now();
                log.info("process#5|monitorPeriod {} in {} ", rangeType, rangeTime);
                if("days".equals(rangeType)){
                    endDate = startDate.plusDays(rangeTime);
                }else if("month".equals(rangeType)){
                    endDate = startDate.plusMonths(rangeTime);
                }
            }
        }

        LocalDateTime finalStartDate = startDate;
        LocalDateTime finalEndDate = endDate;
        log.info("process#5|monitoringEndDate-monitoringStartDate {} {}", finalEndDate, finalStartDate);
        List<PrepaidOfferMembership> memberships = membershipRows
                .stream()
                .map(dataRowDTO ->
                        PrepaidOfferMembership.builder()
                                .offerConfigId(prepaidCxOfferConfig.getId())
//                                .offerSelectionId(prepaidCxOfferSelection.getId())
                                .invocationId(invId)
                                .offerEligibilityTxId(offerEligibilityTxId)
                                .offerDate(offerDate)
                                .monitoringStartDate(finalStartDate)
                                .monitoringEndDate(finalEndDate)
                                .msisdn(Long.valueOf(dataRowDTO.get(1)))
                                .optinFlag("false")
                                .offerMembershipStatus(OfferMembershipStatus.ACTIVE.toString())
                                .build())
                .collect(Collectors.toList());
        //optimize
        for (int i = 0; i < totalObjects; i += batchSize) {
            if (i + batchSize > totalObjects) {
                List<PrepaidOfferMembership> prepaidOfferMemberships = memberships.subList(i, totalObjects);
                List<PrepaidOfferMembership> saved = (List<PrepaidOfferMembership>) prepaidOfferMembershipRepository.saveAll(prepaidOfferMemberships);

                if (ProvisionType.OFFER_MONITORING_WITH_OFFER_ASSIGNMENT.getDescription().equals(prepaidCxOfferConfig.getProvisionType())) {
                    log.info("process#5|NOT Sent to Redemption Queue caused provision type |{}|", prepaidCxOfferConfig.getProvisionType());
                } else if (ProvisionType.DIRECT_PROVISION.getDescription().equals(prepaidCxOfferConfig.getProvisionType()) ||
                        ProvisionType.EVENT_CONDITION_WITH_DIRECT_PROVISION.getDescription().equals(prepaidCxOfferConfig.getProvisionType())
                ) {
                    for (PrepaidOfferMembership p : saved) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("offerMembershipId", p.getId());
                        map.put("msisdn", p.getMsisdn());
                        map.put("smsKeyword", "");
                        map.put("instanceId", prepaidCxOfferConfig.getInstanceId());

                        sendToRedemptionQueue(invId, map);
                    }
                } else {
                    log.info("process#5|NOT Sent to Redemption Queue caused provision type |{}|", prepaidCxOfferConfig.getProvisionType());
                }
                break;
            }

            List<PrepaidOfferMembership> prepaidOfferMemberships = memberships.subList(i, i + batchSize);
            List<PrepaidOfferMembership> saved = (List<PrepaidOfferMembership>) prepaidOfferMembershipRepository.saveAll(prepaidOfferMemberships);


            if (ProvisionType.EVENT_CONDITION.getDescription().equals(prepaidCxOfferConfig.getProvisionType())){
                log.info("process#5|NOT Sent to Redemption Queue caused provision type |{}|", prepaidCxOfferConfig.getProvisionType());
            }
            else if (ProvisionType.OFFER_MONITORING_WITH_OFFER_ASSIGNMENT.getDescription().equals(prepaidCxOfferConfig.getProvisionType())){
                log.info("process#5|NOT Sent to Redemption Queue caused provision type |{}|", prepaidCxOfferConfig.getProvisionType());
            }
            else if (ProvisionType.DIRECT_PROVISION.getDescription().equals(prepaidCxOfferConfig.getProvisionType()) ||
                    ProvisionType.EVENT_CONDITION_WITH_DIRECT_PROVISION.getDescription().equals(prepaidCxOfferConfig.getProvisionType())
            ) {
                for (PrepaidOfferMembership p : saved) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("offerMembershipId", p.getId());
                    map.put("msisdn", p.getMsisdn());
                    map.put("smsKeyword", "");
                    map.put("instanceId", prepaidCxOfferConfig.getInstanceId());
                    sendToRedemptionQueue(invId, map);
                }
            } else {
                log.info("process#5|NOT Sent to Redemption Queue caused provision type |{}|", prepaidCxOfferConfig.getProvisionType());
            }
        }
        log.info("process#5|SAVE|{}|rows|{}", membershipRows.size(), membershipRows);
        //original
//        prepaidOfferMembershipRepository.saveAll(memberships);
    }

    private String parseTrnLogId(String appRowId){
        if(appRowId.contains("|")){
            String[] data  = appRowId.split("|");
            if(data[2] != null){
                return data[2];
            }
        }
        return appRowId;
    }

    @Async
    private void saveToPrepaidOfferMembershipExclus(List<List<String>> membershipExclusRows, String invId, Long offerEligibilityTxId, PrepaidCxOfferConfig prepaidCxOfferConfig, String evaluationType, String evaluationStatus) throws Exception {
        int totalObjects = membershipExclusRows.size();
        Date offerDate = new Date();
        List<PrepaidOfferMembershipExclus> memberships = membershipExclusRows
                .stream()
                .map(dataRowDTO ->
                        PrepaidOfferMembershipExclus.builder()
//                                .offerSelectionId(prepaidCxOfferSelection.getId())
                                .offerDate(offerDate)
                                .invocationId(invId)
                                .offerEligibilityTxId(offerEligibilityTxId)
                                .evaluationType(evaluationType)
                                .evaluationStatus(evaluationStatus)
                                .msisdn(Long.valueOf(dataRowDTO.get(1)))
                                .offerConfigId(prepaidCxOfferConfig.getId())
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
        //type directProvisioning and event + directProvisioning -> invoke redemption
        //type eventConditionOnly -> invoke Custom Event
        //type offerAssignment And Offer Monitoring -> do nothing
        log.info("process#6|START|{}",Constant.QUEUE_NAME_SINGTEL_REDEMPTION);
        log.info("process#6|id|{}|payload|{}",
                invId,
                payload);
        rabbitTemplate.convertAndSend(
                Constant.TOPIC_EXCHANGE_NAME_MEMBERSHIP,
                Constant.QUEUE_NAME_SINGTEL_REDEMPTION,
                payload
        );
        log.info("process#6|END|{}|id|{}",Constant.QUEUE_NAME_SINGTEL_REDEMPTION, invId);
        return ResponseEntity.ok("Success");
    }

    public ResponseEntity<String> sendToCustomEventQueue(String invId, String eventName, List<List<String>> offerLevelRows) {
        Map<String, Object> payload = new HashMap<>();
        List<String> msisdnList = new ArrayList<>();

        for(List<String> m: offerLevelRows){
            String msisdn = m.get(1);
            msisdnList.add(msisdn);
        }
        List<String> listLb = new ArrayList<>();
        listLb.add("1");

        payload.put("responsysCustomEvent", responsysCustomEventUrl);
        payload.put("token", "null");
        payload.put("eventName", eventName);
//        request.put("listName", lbsLocationRequest.getListName());
        payload.put("folderName", folderName);
//        request.put("correlationId", entry2.getKey());

        payload.put("customerId", msisdnList);
//        request.put("status", "MULTIPLE RECIPIENTS FOUND");

        payload.put("list", listLb);
        payload.put("listLbsTargetedId", listLb);

        log.info("process#6|START|{}",Constant.QUEUE_NAME_SINGTEL_RESPONSYS_CUSTOM_EVENT);
        log.info("process#6|id|{}|payload|{}",
                invId,
                payload);
        rabbitTemplate.convertAndSend(
                Constant.TOPIC_EXCHANGE_NAME_MEMBERSHIP,
                Constant.QUEUE_NAME_SINGTEL_RESPONSYS_CUSTOM_EVENT,
                payload
        );
        log.info("process#6|END|{}|id|{}", Constant.QUEUE_NAME_SINGTEL_RESPONSYS_CUSTOM_EVENT, invId);
        return ResponseEntity.ok("Success");
    }


}
