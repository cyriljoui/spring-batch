package com.cyriljoui.spring.poc.batch.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SimpleBatchLikeTask {

    private static final Logger log = LoggerFactory.getLogger(SimpleBatchLikeTask.class);

    private int numberOfExecutions;

    @Scheduled(cron = "*/5 * * * * MON-FRI")
    public void soSimpleScheduledMethod() {
        log.info("soSimpleScheduledMethod numberOfExecutions: {}", numberOfExecutions++);
    }
}
