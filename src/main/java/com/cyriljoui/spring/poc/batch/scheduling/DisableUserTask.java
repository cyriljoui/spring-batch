package com.cyriljoui.spring.poc.batch.scheduling;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DisableUserTask {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job disablePerson;

    @Scheduled(fixedRate = 15000)
    public void disableUser() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters parameter = new JobParametersBuilder()
                .addDate("date", new Date())
                .toJobParameters();

        JobExecution jobExecution = jobLauncher.run(disablePerson, parameter);
    }


}
