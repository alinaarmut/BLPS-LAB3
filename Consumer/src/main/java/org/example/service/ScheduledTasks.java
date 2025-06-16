package org.example.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

@Service
@Slf4j
public class ScheduledTasks implements  Job {
    @Autowired
    private BookingService bookingService;

    @Override
    public void execute(JobExecutionContext context) {
        log.info("Выполнение джобы по расписанию");
        bookingService.cancelExpiredOffers();
    }
}


