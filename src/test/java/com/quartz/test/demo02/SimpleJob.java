package com.quartz.test.demo02;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleJob implements Job {

	private final Logger log = LoggerFactory.getLogger(SimpleJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobKey key = context.getJobDetail().getKey();
		log.info("SimpleJob say: {} executing at {}", key, new Date());
	}

}
