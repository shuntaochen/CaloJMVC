package org.caloch.core;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

public class QuartzJobRunner {

    public void run() throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        JobDetail job = JobBuilder.newJob(QuartzSimpleJob.class)
                .withIdentity("myJob", "group1")
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(40)
                        .repeatForever())
                .build();

        Trigger triggerA = TriggerBuilder.newTrigger()
                .withIdentity("triggerA", "group1")
                .startNow()
                .withPriority(15)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(40)
                        .repeatForever())
                .build();

        Trigger triggerB = TriggerBuilder.newTrigger()
                .withIdentity("triggerB", "group1")
                .startNow()
                .withPriority(10)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(20)
                        .repeatForever())
                .build();

        Trigger misFiredTriggerA = TriggerBuilder.newTrigger()
                .startAt(new Date())
                .build();

        Trigger misFiredTriggerB = TriggerBuilder.newTrigger()
                .startAt(new Date())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withMisfireHandlingInstructionFireNow())
                .build();

        SimpleTrigger trigger1 = (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .startAt(new Date())
                .forJob("job1", "group1")
                .build();

        CronTrigger trigger2 = TriggerBuilder.newTrigger()
                .withIdentity("trigger3", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/2 8-17 * * ?"))
                .forJob("myJob", "group1")
                .build();
    }
}

