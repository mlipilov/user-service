package com.andersen.userservice;

import static org.testcontainers.utility.DockerImageName.parse;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public class ConfiguredIntegrationTest extends BaseIntegrationTest {

  private static final String KAFKA_IMAGE = "confluentinc/cp-kafka:latest";
  private static final String POSTGRES_IMAGE = "postgres:latest";

  private static final String DB_URL = "DB_URL";
  private static final String DB_LOGIN = "DB_LOGIN";
  private static final String DB_PASS = "DB_PASS";
  private static final String KAFKA_SERVER_URL = "KAFKA_ADDRESS";

  protected static final KafkaContainer KAFKA_CONTAINER;
  protected static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER;

  static {
    KAFKA_CONTAINER = new KafkaContainer(parse(KAFKA_IMAGE));
    KAFKA_CONTAINER.start();
    POSTGRESQL_CONTAINER = new PostgreSQLContainer<>(parse(POSTGRES_IMAGE));
    POSTGRESQL_CONTAINER.start();
  }

  @DynamicPropertySource
  static void registerDynamicProperties(DynamicPropertyRegistry registry) {
    registry.add(DB_URL, POSTGRESQL_CONTAINER::getJdbcUrl);
    registry.add(DB_LOGIN, POSTGRESQL_CONTAINER::getUsername);
    registry.add(DB_PASS, POSTGRESQL_CONTAINER::getPassword);
    registry.add(KAFKA_SERVER_URL, KAFKA_CONTAINER::getBootstrapServers);
  }
}
