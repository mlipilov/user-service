package com.andersen.userservice.config.batch;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserCsvTransferJobExecutionListener implements JobExecutionListener {

  @Override
  public void beforeJob(@NonNull final JobExecution jobExecution) {
    log.info("Started {} job", jobExecution.getJobInstance().getJobName());
  }

  @Override
  public void afterJob(@NonNull final JobExecution jobExecution) {
    log.info("Finished {} job", jobExecution.getJobInstance().getJobName());
  }
}
