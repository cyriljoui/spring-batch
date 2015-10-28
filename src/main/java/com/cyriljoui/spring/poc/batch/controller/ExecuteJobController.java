package com.cyriljoui.spring.poc.batch.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class ExecuteJobController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importUserJob;

    @RequestMapping("/job")
    @ResponseBody
    public String executeJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters parameter = new JobParametersBuilder()
                .addDate("date", new Date())
                .toJobParameters();

        JobExecution jobExecution = jobLauncher.run(importUserJob, parameter);
        return jobExecution.getStartTime() + " / " + jobExecution.getEndTime();
    }
}
