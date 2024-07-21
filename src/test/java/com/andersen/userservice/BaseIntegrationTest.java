package com.andersen.userservice;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.springframework.test.context.jdbc.SqlMergeMode.MergeMode.MERGE;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
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
}
