package com.retryengine.job;

import com.retryengine.service.RetryService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
public class RetryJob implements Job {


    @Autowired
    private RetryService retryService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        retryService.pollAndRetry();
    }
}
