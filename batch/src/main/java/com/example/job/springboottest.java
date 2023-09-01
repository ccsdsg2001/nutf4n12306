package com.example.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author cc
 * @date 2023年09月01日 10:45
 */

@EnableScheduling
@Component
public class springboottest implements Job {

    @Scheduled(cron = "0/5 * * * * ?")
    public void ts(){

    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
