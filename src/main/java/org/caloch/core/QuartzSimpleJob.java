package org.caloch.core;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

public class QuartzSimpleJob implements Job {
    static Logger logger = Logger.getLogger(QuartzSimpleJob.class);
    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        String jobSays = dataMap.getString("jobSays");
        float myFloatValue = dataMap.getFloat("myFloatValue");

        logger.info(jobSays+myFloatValue+":"+new Date().getTime());
        System.out.println("Job says: " + jobSays + ", and val is: " + myFloatValue);

        new MailSender().doSend();
    }
}
