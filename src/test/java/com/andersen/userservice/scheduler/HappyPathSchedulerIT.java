package com.andersen.userservice.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.andersen.userservice.ConfiguredIntegrationTest;
import com.andersen.userservice.entity.user.UserEntity;
import com.andersen.userservice.model.user.User;
import com.andersen.userservice.model.workspace.Workspace;
import java.time.Duration;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaOperations.OperationsCallback;

@SpringBatchTest
public class HappyPathSchedulerIT extends ConfiguredIntegrationTest {

  private static final String COMPLETED = "COMPLETED";

  @Autowired
  @Qualifier("jobLauncherTestUtils")
  private JobLauncherTestUtils jobLauncherTestUtils;
  @Autowired
  private Job userJob;
  @Autowired
  private JobLauncher jobLauncher;

  @Test
  @SneakyThrows
  void whenJobIsStarted_AndThereAreMessagesInKafka_ThenProcess() {
    final User user = sendUserToKafka();
    launchJob();
    performAssertions(user);
  }

  @SneakyThrows
  private User sendUserToKafka() {
    final Workspace workspace = new Workspace();
    workspace.setName("name");
    final User user = new User();
    user.setEmail("email@email.com");
    user.setWorkspaces(List.of(workspace));
    userKafkaTemplate.setDefaultTopic("user-csv-data");
    userKafkaTemplate.executeInTransaction(
        (OperationsCallback<String, User, Object>) operations
            -> operations.sendDefault("", user));
    Thread.sleep(10_000L);
    return user;
  }

  private void launchJob() throws Exception {
    jobLauncherTestUtils.setJob(userJob);
    jobLauncherTestUtils.setJobLauncher(jobLauncher);
    final JobExecution jobExecution = jobLauncherTestUtils.launchJob();
    assertEquals(COMPLETED, jobExecution.getExitStatus().getExitCode());
  }

  private void performAssertions(final User user) {
    await().atMost(Duration.ofSeconds(60)).untilAsserted(() -> {
      final var optionalUserEntity = userEntityRepository.findByEmail(user.getEmail());
      assertThat(optionalUserEntity).isPresent();
      final UserEntity userEntity = optionalUserEntity.get();
      assertThat(userEntity.getUserContactDetails().getEmail()).isEqualTo(user.getEmail());
    });
  }
}
