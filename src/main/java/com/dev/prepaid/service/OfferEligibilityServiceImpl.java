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

    @Override
    public List<List<String>> processData(List<List<String>> rows,
                                          InvocationRequest invocation,
                                          InvocationRequest invocationOri,
                                          PrepaidCxOfferConfig instanceConfiguration,
                                          String groupId,
                                          Long dataSetSize) throws Exception {

        log.info("FindBatchId|Request|{}|{}", invocationOri.getUuid(), invocation.getBatchId());
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
            saveToPrepaidOfferMembership(offerLevelRows, invocation.getUuid(), invocation.getOfferEligibilityTxId(), instanceConfiguration);
        }


        //7
        productImportEndpoint(offerLevelRows, invocation, invocationOri, instanceConfiguration);

        try {
            PrepaidCxProvInvocations prepaidCxProvInvocations = prepaidCxProvInvocationsRepository.findOneById(invocation.getUuid());
            prepaidCxProvInvocations.setStatus("COMPLETED");
            prepaidCxProvInvocationsRepository.save(prepaidCxProvInvocations);
        } catch (Exception e) {
            log.error("ERROR : {}", e);
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
        String[] excludeOverallOfferName = null;
        if (!opsFind.isPresent()) {
            log.info("process#1|SUMMARY_IN|{}|rows|{}", rows.size(), rows);
            log.info("process#1|SUMMARY_OUT|{}|rows|{}", rows.size(), rows);
            log.info("process#1|evaluationSubscriberExclusion|END");
            return rows;
        }
        for (List<String> row : rows) {
            if (opsFind.get().getExcludeProgramId() != null) {
                excludeOverallOfferName = opsFind.get().getExcludeProgramId().split(",");
                boolean checkIsExist = false;
                for (String overallOfferName : excludeOverallOfferName) {
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
                }

                if (!checkIsExist) {
                    log.info("process#1|1|EXCLUSION|{}|PASS", row.get(1));
                    resultRows.add(row);
                } else {
                    log.info("process#1|1|EXCLUSION|{}|NOT_PASS", row.get(1));
                    excluseRows.add(row);
                }

            } else {
                log.info("process#1|3|EXCLUSION|{}|PASS", row.get(1));
                resultRows.add(row);
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

        log.info("process#2|IS_FREQUENCY_ONLY|{}|VALUE|{}", e.getIsFrequencyOnly(), e.getFrequency());
        log.info("process#2|IS_FREQUENCY_AND_TIME|{}|VALUE|{} IN {} Days", e.getIsFrequencyAndTime(), e.getNumberOfFrequency(), e.getNumberOfDays());
        for (List<String> row : rows) {
            if (subscriberLevel(row.get(1), e)) {
                log.info("process#2|SUBSCRIBER_LEVEL|msisdn|{}|PASS", row.get(1));
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
        List<List<String>> resultRows = new ArrayList<>();
        log.info("process#3|evaluationAdvanceFilter|START|type|{}", instanceConfiguration.getProvisionType());
        log.info("process#3|DATA|{}", rows);
        log.info("process#3|SUMMARY_IN|{}|", rows.size());
        log.info("process#3|SUMMARY_OUT|{}|rows|{}", rows.size(), rows);
        log.info("process#3|SUMMARY_EXCLUSE|{}|rows|{}", resultRows.size(), resultRows);
        if (resultRows.size() > 0) {
            saveToPrepaidOfferMembershipExclus(resultRows, invocation.getUuid(), invocation.getOfferEligibilityTxId(), instanceConfiguration,
                    "evaluationAdvanceFilter",
                    "FAILED");
        }
        log.info("process#3|evaluationAdvanceFilter|END");
        return rows;
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

        if (rows.size() > 0) {
            if (prepaidCxOfferEligibility.getIsOfferLevelCapOnly()) {
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
            } else if (prepaidCxOfferEligibility.getIsOfferLevelCapAndPeriod()) {
                int currentCap = countOfferCapPerOfferConfigIdAndRangePeriod(instanceConfiguration.getId(), prepaidCxOfferEligibility);
                log.info("process#4|eligible|{}|currentCap|{}|maximumCapValue|{}|in|{}|period_days",
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

        DataSet dataSet = DataSet.builder()
                .rows(rows)
                .id(invocation.getDataSet().getId())
                .size(Long.valueOf(rows.size()))
                .build();

        InvocationRequest newInvocationRequest = InvocationRequest.builder()
                .uuid(invocation.getUuid())
                .instanceContext(invocation.getInstanceContext())
                .onCompletionCallbackEndpoint(invocation.getOnCompletionCallbackEndpoint())
                .productExportEndpoint(invocation.getProductExportEndpoint())
                .productImportEndpoint(invocation.getProductImportEndpoint())
                .maxPushBatchSize(invocation.getMaxPushBatchSize())
                .maxPullPageSize(invocation.getMaxPullPageSize())
                .dataSet(dataSet)
                .build();


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
            Map<String, Object> output = invocation.getInstanceContext().getRecordDefinition().generateOutputRowAsNewMap(input);
            List<String> listOutput = List.of(
                    output.get("appcloud_row_correlation_id").toString(), //appcloud_row_correlation_id
                    "success", //appcloud_row_status
                    "", //appcloud_row_errormessage
                    instanceConfiguration.getOverallOfferName(),
                    "success"); //STATUS
            rows.add(listOutput);
        }

        DataImportDTO data = DataImportDTO.builder()
                .fieldDefinitions(InitData.recordDefinition.getOutputParameters())
                .dataSet(dataSet)
                .build();
        try {
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
        start.add(Calendar.DATE, -prepaidCxOfferEligibility.getOfferLevelCapPeriodDays().intValue());
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
        Date startDate = null;
        Date endDate = null;
        Optional<PrepaidCxOfferMonitoring> opsFind = prepaidCxOfferMonitoringRepository.findByOfferConfigId(prepaidCxOfferConfig.getId());
        if (opsFind.isPresent()) {
            startDate = opsFind.get().getPeriodStartDate();
            endDate = opsFind.get().getPeriodEndDate();
        }
        Date finalStartDate = startDate;
        Date finalEndDate = endDate;
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
                // getConfigAdvanceFilterQuery with parameter offerConfigId
                // getMsisdn from query
                // compare msisdn eligible with msisdn from advance filter query
                // match msisdn put in queue redemption
                if(ProvisionType.DIRECT_PROVISION.getDescription().equals(prepaidCxOfferConfig.getProvisionType()) ||
                        ProvisionType.OFFER_MONITORING_WITH_OFFER_ASSIGNMENT.equals(prepaidCxOfferConfig.getProvisionType())
                ) {
                    for (PrepaidOfferMembership p : saved) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("offerMembershipId", p.getId());
                        map.put("msisdn", p.getMsisdn());
                        map.put("smsKeyword", "");
                        map.put("instanceId", prepaidCxOfferConfig.getInstanceId());

                        sendToRedemptionQueue(invId, map);
                        }
                }
                break;
            }
            List<PrepaidOfferMembership> prepaidOfferMemberships = memberships.subList(i, i + batchSize);
            List<PrepaidOfferMembership> saved = (List<PrepaidOfferMembership>) prepaidOfferMembershipRepository.saveAll(prepaidOfferMemberships);
            if(ProvisionType.DIRECT_PROVISION.getDescription().equals(prepaidCxOfferConfig.getProvisionType()) ||
                    ProvisionType.OFFER_MONITORING_WITH_OFFER_ASSIGNMENT.equals(prepaidCxOfferConfig.getProvisionType())
            ) {
                for (PrepaidOfferMembership p : saved) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("offerMembershipId", p.getId());
                    map.put("msisdn", p.getMsisdn());
                    map.put("smsKeyword", "");
                    map.put("instanceId", prepaidCxOfferConfig.getInstanceId());
                    sendToRedemptionQueue(invId, map);
                }
            }
        }
        log.info("process#5|SAVE|{}|rows|{}", membershipRows.size(), membershipRows);
        //original
//        prepaidOfferMembershipRepository.saveAll(memberships);
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
        log.info("process#6|START|sendToRedemptionQueue");
        log.info("process#6|id|{}|payload|{}",
                invId,
                payload);
        rabbitTemplate.convertAndSend(
                Constant.TOPIC_EXCHANGE_NAME_MEMBERSHIP,
                Constant.QUEUE_NAME_SINGTEL_REDEMPTION,
                payload
        );
        log.info("process#6|END|sendToRedemptionQueue|id|{}", invId);
        return ResponseEntity.ok("Success");
    }

}
