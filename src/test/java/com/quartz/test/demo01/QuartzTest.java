package com.quartz.test.demo01;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzTest {

	public static void main(String[] args) throws SchedulerException, InterruptedException {

		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		scheduler.start();
		JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity("job1", "group1").build();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever()).build();
		scheduler.scheduleJob(job, trigger);
		Thread.sleep(60000);
		scheduler.shutdown();
	}

}
