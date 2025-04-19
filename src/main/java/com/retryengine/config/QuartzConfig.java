package com.retryengine.config;

import com.retryengine.job.RetryJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class QuartzConfig {



    @Bean
    public Trigger retryJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(retryJobDetail())
                .withIdentity("retryJobTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?")) // every minute
                .build();
    }
    @Bean
    public JobDetail retryJobDetail() {
        return JobBuilder.newJob(RetryJob.class)
                .withIdentity("retryJob")
                .storeDurably()
                .build();
    }
    @Bean
    public Job retryJob() {
        return new RetryJob();
    }
}
