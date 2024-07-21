package com.andersen.userservice.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.andersen.userservice.ConfiguredIntegrationTest;
import com.andersen.userservice.entity.user.UserEntity;
import com.andersen.userservice.model.user.User;
import com.andersen.userservice.model.workspace.Workspace;
import com.andersen.userservice.repository.UserEntityRepository;
import java.time.Duration;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaOperations.OperationsCallback;
import org.springframework.kafka.core.KafkaTemplate;

public class UserCsvTransferJobSchedulerIT extends ConfiguredIntegrationTest {

  @Autowired
  private KafkaTemplate<String, User> userKafkaTemplate;
  @Autowired
  private UserEntityRepository userEntityRepository;

  @Test
  @SneakyThrows
  void whenJobIsStarted_AndThereAreMessagesInKafka_ThenProcess() {
    final Workspace workspace = new Workspace();
    workspace.setName("name");
    final User user = new User();
    user.setEmail("email@email.com");
    user.setWorkspaces(List.of(workspace));
    userKafkaTemplate.setDefaultTopic("user-csv-data");
    userKafkaTemplate.executeInTransaction(
        (OperationsCallback<String, User, Object>) operations
            -> operations.sendDefault("", user));

    await().atMost(Duration.ofSeconds(60)).untilAsserted(() -> {
      final var optionalUserEntity = userEntityRepository.findByEmail(user.getEmail());
      assertThat(optionalUserEntity).isPresent();
      final UserEntity userEntity = optionalUserEntity.get();
      assertThat(userEntity.getUserContactDetails().getEmail()).isEqualTo("email@email.com");
    });
  }
}
