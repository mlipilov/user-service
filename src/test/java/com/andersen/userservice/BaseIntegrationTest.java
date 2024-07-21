package com.andersen.userservice;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.springframework.test.context.jdbc.SqlMergeMode.MergeMode.MERGE;

import com.andersen.userservice.model.user.User;
import com.andersen.userservice.repository.UserEntityRepository;
import com.andersen.userservice.repository.UserWriteErrorEntityRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlMergeMode;

@ActiveProfiles(profiles = "integration")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Sql(
    scripts = "classpath:truncate.sql",
    executionPhase = AFTER_TEST_METHOD,
    config = @SqlConfig(transactionMode = ISOLATED))
@SqlMergeMode(MERGE)
public abstract class BaseIntegrationTest {

  @LocalServerPort
  protected int port;

  @SpyBean
  protected UserEntityRepository userEntityRepository;
  @Autowired
  protected KafkaTemplate<String, User> userKafkaTemplate;
  @Autowired
  protected UserWriteErrorEntityRepository writeErrorEntityRepository;
  @Autowired
  protected JobLauncher jobLauncher;
  @Autowired
  protected Job myBatchJob;
}
