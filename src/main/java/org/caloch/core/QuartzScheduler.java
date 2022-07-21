package org.caloch.core;

import org.apache.log4j.BasicConfigurator;
//import org.quartz.CronScheduleBuilder;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;


public class QuartzScheduler {
    private static final String NAME_OF_JOB = "SendEmailsToSubscribed";
    private static final String NAME_OF_GROUP = "cronJobs";
    private static final String NAME_OF_TRIGGER = "triggerStart";
    private static Scheduler scheduler;

    public void run() throws Exception {

        BasicConfigurator.configure();
        System.out.println(" The email of the QuartzScheduler main thread is: " + Thread.currentThread().getName());
        scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        Trigger triggerCron = createCronTrigger();
        scheduleJob(triggerCron);
    }

    private static void scheduleJob(Trigger triggerNew) throws Exception {
        JobDetail jobInstance = JobBuilder.newJob(QuartzSimpleJob.class).withIdentity(NAME_OF_JOB, NAME_OF_GROUP)
                .usingJobData("jobSays", "sending emails to subscribed")
                .usingJobData("myFloatValue", 0.03f)
                .build();
        scheduler.scheduleJob(jobInstance, triggerNew);
    }

    private static Trigger createCronTrigger() {
        String CRON_EXPRESSION = "0 * * * * ?";
        Trigger triggerNew = TriggerBuilder.newTrigger().withIdentity(NAME_OF_TRIGGER, NAME_OF_GROUP)
                .withSchedule(CronScheduleBuilder.cronSchedule(CRON_EXPRESSION)).build();
        return triggerNew;
    }

    private static Trigger createTrigger() {
        int TIME_INTERVAL = 60 * 60 * 6;
        Trigger triggerNew = TriggerBuilder.newTrigger().withIdentity(NAME_OF_TRIGGER, NAME_OF_GROUP)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(TIME_INTERVAL).repeatForever())
                .build();
        return triggerNew;
    }
}