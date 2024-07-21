package com.andersen.userservice.scheduler;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.when;

import com.andersen.userservice.ConfiguredIntegrationTest;
import com.andersen.userservice.entity.user.UserContactDetails;
import com.andersen.userservice.entity.user.UserEntity;
import com.andersen.userservice.model.user.User;
import com.andersen.userservice.model.workspace.Workspace;
import java.time.Duration;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.Chunk;
import org.springframework.kafka.core.KafkaOperations.OperationsCallback;

public class UserCsvTransferJobSchedulerIT extends ConfiguredIntegrationTest {

  @Test
  @SneakyThrows
  void whenJobIsStarted_AndThereAreMessagesInKafka_ButThereIsWriteError_ThenSaveError() {
    final UserEntity entity = new UserEntity();
    final UserContactDetails contactDetails = new UserContactDetails();
    contactDetails.setEmail("email@email.com");
    entity.setUserContactDetails(contactDetails);
    final Chunk<UserEntity> users = new Chunk<>(entity);
    when(userEntityRepository.saveAll(users)).thenThrow(new RuntimeException("error"));

    final Workspace workspace = new Workspace();
    workspace.setName("name");
    final User user = new User();
    user.setEmail("email@email.com");
    user.setWorkspaces(List.of(workspace));
    userKafkaTemplate.setDefaultTopic("user-csv-data");
    userKafkaTemplate.executeInTransaction(
        (OperationsCallback<String, User, Object>) operations
            -> operations.sendDefault("", user));

    await().atMost(Duration.ofSeconds(100))
        .until(() -> (writeErrorEntityRepository.findAll().size() == 1));
  }
}
