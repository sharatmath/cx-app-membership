package com.dev.prepaid.schedule;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dev.prepaid.service.OfferService;
import com.dev.prepaid.service.RedemptionService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScheduledTask {
	
	@Autowired
	private OfferService offerService;
	
	@Autowired
	private RedemptionService redemptionService;
	
	@Scheduled(cron = "${tasks.scheduled.5minute}", zone = "${tasks.scheduled.zone}") /* every 5 minute */
	public void scheduler() throws ParseException {
		offerService.evictAllCaches();
	}
	
	@Scheduled(cron = "${tasks.scheduled.minutely}", zone = "${tasks.scheduled.zone}") /* every 1 minute */
	public void redemptionScheduler() throws ParseException {
		redemptionService.processByCall(null);
	}
}
