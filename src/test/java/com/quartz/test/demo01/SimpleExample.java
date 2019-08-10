package com.quartz.test.demo01;

import java.util.Date;

import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quartz.test.util.SchedulerFactoryUtil;

public class SimpleExample {

	private final Logger log = LoggerFactory.getLogger(SimpleExample.class);

	public void run() throws Exception {
		log.info("---------------初始化--------------");
		Scheduler scheduler = SchedulerFactoryUtil.getScheduler();
		log.info("---------------初始化完成--------------");
		Date runTime = DateBuilder.evenMinuteDate(new Date());
		log.info("-------------------Schedulering Job------------------");
		JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity("job1", "group1").build();
		Trigger trigger = TriggerBuilder.newTrigger().startAt(runTime).withIdentity("trigger1", "group1").build();
		scheduler.scheduleJob(job, trigger);
		log.info("{} will run at: {}", job.getKey(), runTime);
		scheduler.start();
		log.info("----------开启定时任务---------");
		log.info("--------------等待65秒----------------");
		Thread.sleep(65000);
		log.info("--------------定时任务关闭--------------");
		
		scheduler.shutdown();
		log.info("----------------定时任务关闭完成----------------");
		
	}
	
	public static void main(String[] args) throws Exception {
		SimpleExample se = new SimpleExample();
		se.run();
	}

}
