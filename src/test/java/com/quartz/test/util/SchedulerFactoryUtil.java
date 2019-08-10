package com.quartz.test.util;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerFactoryUtil {

	private final static SchedulerFactory sf = new StdSchedulerFactory();

	public static Scheduler getScheduler() throws SchedulerException {
		return sf.getScheduler();
	}

}
