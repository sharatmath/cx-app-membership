package com.dev.prepaid.service;

import com.dev.prepaid.domain.*;
import com.dev.prepaid.model.invocation.InvocationRequest;
import com.dev.prepaid.repository.*;
import com.dev.prepaid.util.BaseRabbitTemplate;
import com.dev.prepaid.util.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OfferEligibilityServiceImpl extends BaseRabbitTemplate implements OfferEligibilityService {
    static final String DATE_FORMAT = "yyyyMMdd";
    static final String DATETIME_FORMAT = "yyyyMMdd'|'HHmmss";

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
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

    @Override
    public List<List<String>> processData(List<List<String>> rows,
                            InvocationRequest invocation,
                            PrepaidCxOfferConfig instanceConfiguration,
                            String groupId,
                            Long dataSetSize) throws Exception{
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

        return eligibleRows;
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

    private void processOfferLevelCap(List<List<String>> eligibleRows, PrepaidCxOfferEligibility prepaidCxOfferEligibility, String invId, PrepaidCxOfferConfig prepaidCxOfferConfig, PrepaidCxOfferSelection prepaidCxOfferSelection) throws Exception{
        List<List<String>> offerMembershipRows = new ArrayList<>();
        List<List<String>> offerMembershipExcluseRows = new ArrayList<>();

        if(prepaidCxOfferEligibility.getIsOfferLevelCapOnly()){
            int currentCap = countOfferCapPerOfferConfigId(prepaidCxOfferConfig.getId());
            log.info("invId|{}|confId|{}|processLevelCapOnly|eligible|{}|currentCap|{}|maximumCapValue|{}", invId, prepaidCxOfferConfig.getId(), eligibleRows.size(), currentCap, prepaidCxOfferEligibility.getOfferLevelCapValue());
            if(currentCap >=  prepaidCxOfferEligibility.getOfferLevelCapValue()){
                offerMembershipExcluseRows = eligibleRows;
            }else{
                int capacityCap = prepaidCxOfferEligibility.getOfferLevelCapValue().intValue() - currentCap;
                if(eligibleRows.size() <= capacityCap){
                    offerMembershipRows = eligibleRows;
                }else {
                    offerMembershipRows = eligibleRows.subList(0, capacityCap);
                    offerMembershipExcluseRows = eligibleRows.subList(capacityCap, eligibleRows.size());
                }
            }
        }else if(prepaidCxOfferEligibility.getIsOfferLevelCapAndPeriod()){
            int currentCap = countOfferCapPerOfferConfigIdAndRangePeriod(prepaidCxOfferConfig.getId(), prepaidCxOfferEligibility);
            log.info("invId|{}|confId|{}|processLevelCapAndPeriod|eligible|{}|currentCap|{}|maximumCapValue|{}|in|{}|period_days",invId, prepaidCxOfferConfig.getId(), eligibleRows.size(), currentCap, prepaidCxOfferEligibility.getOfferLevelCapPeriodValue(), prepaidCxOfferEligibility.getOfferLevelCapPeriodDays());
            if(currentCap >= prepaidCxOfferEligibility.getOfferLevelCapPeriodValue()){
                offerMembershipRows = eligibleRows;
            }else{
                int capacityCap = prepaidCxOfferEligibility.getOfferLevelCapPeriodValue().intValue() - currentCap;
                offerMembershipRows = eligibleRows.subList(0, capacityCap);
                offerMembershipExcluseRows = eligibleRows.subList(capacityCap, eligibleRows.size());
            }
        }
        log.info("invId|{}|confId|{}|processLevelCapOnly|eligible|{}|membership|{}|membership_exclus|{}",invId, prepaidCxOfferConfig.getId(), eligibleRows.size(), offerMembershipRows.size(), offerMembershipExcluseRows.size());
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

    private void saveToPrepaidOfferMembership(List<List<String>> membershipRows, String invId, PrepaidCxOfferConfig prepaidCxOfferConfig, PrepaidCxOfferSelection prepaidCxOfferSelection) throws Exception{
        int totalObjects = membershipRows.size();
        log.info("invId|{}|confId|{}|total|save_to|membership|{}",invId, prepaidCxOfferConfig.getId(), totalObjects);
        List<PrepaidOfferMembership> memberships = membershipRows
                .stream()
                .map(dataRowDTO ->
                        PrepaidOfferMembership.builder()
                                .offerConfigId(prepaidCxOfferConfig.getId())
                                .offerSelectionId(prepaidCxOfferSelection.getId())
                                .msisdn(Long.valueOf(dataRowDTO.get(0)))
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
    private void saveToPrepaidOfferMembershipExclus(List<List<String>> membershipExclusRows, String invId, PrepaidCxOfferConfig prepaidCxOfferConfig, PrepaidCxOfferSelection prepaidCxOfferSelection) throws Exception {
        int totalObjects = membershipExclusRows.size();
        log.info("invId|{}|confId|{}|total|save_to|membership_exclus|{}",invId, prepaidCxOfferConfig.getId(), totalObjects);
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

}
