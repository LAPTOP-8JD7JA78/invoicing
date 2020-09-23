package com.smartech.invoicing.scheduler;

import org.springframework.scheduling.annotation.Scheduled;

public class SchedulerService {
	
	@Scheduled(fixedDelay=1000, initialDelay=1000)
	public void testSchedule() {
		System.out.println("TEST");
	}

}
