package com.andersen.userservice.scheduler;

import static java.util.concurrent.TimeUnit.MINUTES;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * UserCsvTransferJobScheduler is a component responsible for scheduling and executing a batch job
 * to transfer user data in CSV format. It uses Spring's JobLauncher and Job interfaces to launch
 * and run the batch job.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserCsvTransferJobScheduler {

  private static final String JOB_NAME = "MigrateUserDataJob";

  private final JobLauncher jobLauncher;
  private final Job myBatchJob;

  /**
   * Schedules and executes a batch job to transfer user data in CSV format.
   */
  @Scheduled(fixedDelay = 1L, timeUnit = MINUTES)
  @SneakyThrows
  public void scheduleUserCsvTransferJob() {
    log.info("Scheduling user csv transfer job");
    final JobParameters params = new JobParametersBuilder()
        .addString(JOB_NAME, UUID.randomUUID().toString())
        .toJobParameters();
    jobLauncher.run(myBatchJob, params);
    log.info("Job completed successfully");
  }
}