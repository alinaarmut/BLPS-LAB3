package org.example.config;

import org.example.service.ScheduledTasks;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.config.ScheduledTask;

@Configuration
public class QuartzSchedulerConfig {

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(ScheduledTasks.class)
                .withIdentity("cancelExpiredOffersJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger trigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("dailyTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInHours(24)
                        .repeatForever())
                .build();
    }
}


