package com.dev.prepaid.schedule;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dev.prepaid.service.OfferService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScheduledTask {
	
	@Autowired
	private OfferService offerService;
	
	@Scheduled(cron = "${tasks.scheduled.5minute}", zone = "${tasks.scheduled.zone}") /* every 5 minute */
	public void scheduler() throws ParseException {
		offerService.evictAllCaches();
	}
}
