package com.quartz.test.demo02;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quartz.test.util.SchedulerFactoryUtil;

public class SimpleTriggerExample {

	private final Logger log = LoggerFactory.getLogger(SimpleTriggerExample.class);

	public void run() throws Exception {
		log.info("---------------初始化---------------");
		Scheduler scheduler = SchedulerFactoryUtil.getScheduler();
		log.info("---------------初始化完成---------------");
		log.info("---------------生成任务---------------");
		Date startTime = DateBuilder.nextGivenSecondDate(null, 15);
		// 任务1执行一次
		JobDetail job = JobBuilder.newJob(SimpleJob.class).withIdentity("job1", "group1").build();
		SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
				.startAt(startTime).build();
		Date ft = scheduler.scheduleJob(job, trigger);
		log.info("{} will run at: {} and repeat: {} times, every {} seconds", job.getKey(), ft,
				trigger.getRepeatCount(), trigger.getRepeatInterval() / 1000);
		// 任务2执行一次
		job = JobBuilder.newJob(SimpleJob.class).withIdentity("job2", "group1").build();
		trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("trigger2", "group1").startAt(startTime)
				.build();
		ft = scheduler.scheduleJob(job, trigger);
		log.info("{} will run at: {} and repeat: {} times, every {} seconds", job.getKey(), ft,
				trigger.getRepeatCount(), trigger.getRepeatInterval() / 1000);
		// 任务3执行11次，每10秒执行一次
		job = JobBuilder.newJob(SimpleJob.class).withIdentity("job3", "group1").build();
		trigger = TriggerBuilder.newTrigger().withIdentity("trigger3", "group1").startAt(startTime)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(10))
				.build();
		ft = scheduler.scheduleJob(job, trigger);
		log.info("{} will run at: {} and repeat: {} times, every {} seconds", job.getKey(), ft,
				trigger.getRepeatCount(), trigger.getRepeatInterval() / 1000);
		// 修改任务3的触发调度
		trigger = TriggerBuilder.newTrigger().withIdentity("trigger3", "group2").startAt(startTime)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(2))
				.build();
//		ft = scheduler.scheduleJob(trigger.getKey(),trigger);
		log.info("{} will also run at: {} and repeat: {} times, every {} seconds", job.getKey(), ft,
				trigger.getRepeatCount(), trigger.getRepeatInterval() / 1000);

		// 任务4——每10秒运行一次，运行六次
		job = JobBuilder.newJob(SimpleJob.class).withIdentity("job4", "group1").build();
		trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("trigger4", "group1").startAt(startTime)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(5))
				.build();
		ft = scheduler.scheduleJob(job, trigger);
		log.info("{} will also run at: {} and repeat: {} times, every {} seconds", job.getKey(), ft,
				trigger.getRepeatCount(), trigger.getRepeatInterval() / 1000);
		// 任务5 运行一次 5分钟以后运行
		job = JobBuilder.newJob(SimpleJob.class).withIdentity("job5", "group1").build();
		trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("trigger5", "group1")
				.startAt(DateBuilder.futureDate(5, IntervalUnit.MINUTE)).build();
		ft = scheduler.scheduleJob(job, trigger);
		log.info("{} will also run at: {} and repeat: {} times, every {} seconds", job.getKey(), ft,
				trigger.getRepeatCount(), trigger.getRepeatInterval() / 1000);
		// 任务6 一直运行下去，每40秒1次
		job = JobBuilder.newJob(SimpleJob.class).withIdentity("job6", "group1").build();
		trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("trigger6", "group1").startAt(startTime)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(40).repeatForever()).build();
		ft = scheduler.scheduleJob(job, trigger);
		log.info("{} will also run at: {} and repeat: {} times, every {} seconds", job.getKey(), ft,
				trigger.getRepeatCount(), trigger.getRepeatInterval() / 1000);

		log.info("-------Starting scheduler-------");

		scheduler.start();

		log.info("-------Started scheduler-------");

		job = JobBuilder.newJob(SimpleJob.class).withIdentity("job7", "group1").build();
		trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("trigger7", "group1").startAt(startTime)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(5).withRepeatCount(20))
				.build();
		ft = scheduler.scheduleJob(job, trigger);
		log.info("{} will also run at: {} and repeat: {} times, every {} seconds", job.getKey(), ft,
				trigger.getRepeatCount(), trigger.getRepeatInterval() / 1000);

		job = JobBuilder.newJob(SimpleJob.class).withIdentity("job8", "group1").storeDurably().build();

		scheduler.addJob(job, true);
		log.info("'Manually' triggering job8...");

		scheduler.triggerJob(JobKey.jobKey("job8", "group1"));

		log.info("------- Waiting 30 seconds... --------------");

		Thread.sleep(30L * 1000);

		log.info("------- Rescheduling... --------------------");
		trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("trigger7", "group1").startAt(startTime)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(5).withRepeatCount(20))
				.build();
		ft = scheduler.rescheduleJob(trigger.getKey(), trigger);
		log.info("job7 rescheduled to run at: {}", ft);

		log.info("------- Waiting five minutes... ------------");

		Thread.sleep(5L * 60 * 1000);
		log.info("------- Shutting Down ---------------------");
		scheduler.shutdown(true);
		log.info("------- Shutdown Complete -----------------");
		SchedulerMetaData smd = scheduler.getMetaData();
		log.info("Executed {} jobs.", smd.getNumberOfJobsExecuted());

	}

	public static void main(String[] args) throws Exception {
		SimpleTriggerExample ste = new SimpleTriggerExample();
		ste.run();
	}

}
