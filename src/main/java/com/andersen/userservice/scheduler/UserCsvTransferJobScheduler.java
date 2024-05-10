package com.andersen.userservice.scheduler;

import static java.util.concurrent.TimeUnit.MINUTES;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCsvTransferJobScheduler {

  private final JobLauncher jobLauncher;
  private final Job myBatchJob;

  @Scheduled(fixedDelay = 1L, timeUnit = MINUTES)
  @SneakyThrows
  public void scheduleUserCsvTransferJob() {
    log.info("Scheduling user csv transfer job");
    jobLauncher.run(myBatchJob, new JobParameters());
    log.info("Job completed successfully");
  }
}