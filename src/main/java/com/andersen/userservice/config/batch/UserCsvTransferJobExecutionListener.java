package com.andersen.userservice.config.batch;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

/**
 * UserCsvTransferJobExecutionListener is a class that implements the JobExecutionListener
 * interface. It provides methods to be called before and after a job execution.
 */
@Slf4j
@Component
public class UserCsvTransferJobExecutionListener implements JobExecutionListener {

  /**
   * This method is called before a job execution. It logs the start of the job using the job name
   * obtained from the JobInstance.
   *
   * @param jobExecution The JobExecution object representing the current job execution.
   */
  @Override
  public void beforeJob(@NonNull final JobExecution jobExecution) {
    log.info("Started {} job", jobExecution.getJobInstance().getJobName());
  }

  /**
   * This method is called after a job execution. It logs the completion of the job using the job name obtained from the JobInstance.
   *
   * @param jobExecution The JobExecution object representing the current job execution.
   */
  @Override
  public void afterJob(@NonNull final JobExecution jobExecution) {
    log.info("Finished {} job", jobExecution.getJobInstance().getJobName());
  }
}
