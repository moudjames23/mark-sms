package com.moudjames23.marksms.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomerImportBatchLauncher {


    private final JobLauncher jobLauncher;

    @Qualifier("customer_job")
    private final Job job;

    public void run(String filePath) throws JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException,
            JobParametersInvalidException,
            JobRestartException {

        JobParameters jobParameters  = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .addString("filePath", filePath)
                .toJobParameters();

        jobLauncher.run(job, jobParameters);
    }

}
