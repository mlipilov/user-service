package com.andersen.userservice.config.batch;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

import com.andersen.userservice.entity.user.UserEntity;
import com.andersen.userservice.model.user.User;
import com.andersen.userservice.repository.UserEntityRepository;
import java.util.Properties;
import java.util.UUID;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.kafka.KafkaItemReader;
import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Configuration class for transferring CSV user data from Kafka to a database.
 */
@Configuration
public class UserCsvDataTransferConfig {

  private static final int CHUNK_SIZE = 100;

  private static final String USER_JOB_NAME = "transferCsvUserDataJob";
  private static final String USER_JOB_STEP_NAME = "transferCsvUserDataStep";
  private static final String USER_KAFKA_ITEM_READER_NAME = "UserKafkaItemReaderName";
  public static final String DELIMITER = "_";

  /**
   * Creates a KafkaItemReader for reading User objects from a Kafka topic.
   *
   * @param topic           The name of the Kafka topic.
   * @param kafkaProperties The KafkaProperties object containing the Kafka consumer properties.
   * @return A KafkaItemReader instance for reading User objects from the specified topic.
   */
  @Bean
  @StepScope
  public KafkaItemReader<String, User> userReader(
      @Value("${spring.kafka.topics.user-csv-data-topic}") final String topic,
      final KafkaProperties kafkaProperties
  ) {
    final SslBundles sslBundles = new DefaultSslBundleRegistry();
    final var consumerProps = kafkaProperties.buildConsumerProperties(sslBundles);
    final Properties properties = new Properties();
    properties.putAll(consumerProps);
    return new KafkaItemReaderBuilder<String, User>()
        .topic(topic)
        .name(USER_KAFKA_ITEM_READER_NAME)
        .partitions(INTEGER_ZERO)
        .consumerProperties(properties)
        .build();
  }

  /**
   * Returns a RepositoryItemWriter for writing UserEntity objects to a repository.
   *
   * @param userEntityRepository The repository where the UserEntity objects will be written.
   * @return A RepositoryItemWriter instance for UserEntity objects.
   */
  @Bean
  public RepositoryItemWriter<UserEntity> userWriter(
      final UserEntityRepository userEntityRepository
  ) {
    return new RepositoryItemWriterBuilder<UserEntity>()
        .repository(userEntityRepository)
        .build();
  }

  /**
   * Returns a Step object for a user job.
   *
   * @param jobRepository              The JobRepository object used for managing job metadata and
   *                                   status.
   * @param reader                     The KafkaItemReader used for reading User objects from a
   *                                   Kafka topic.
   * @param writer                     The RepositoryItemWriter used for writing UserEntity objects
   *                                   to a repository.
   * @param platformTransactionManager The PlatformTransactionManager used for managing
   *                                   transactions.
   * @param userItemProcessor          The UserItemProcessor used for processing User objects.
   * @param userItemWriteListener      The UserItemWriteListener used for handling write errors.
   * @param userItemReadListener       The UserItemReadListener used for handling read errors.
   * @return A Step object for a user job.
   */
  @Bean
  public Step userJobStep(
      final JobRepository jobRepository,
      final KafkaItemReader<String, User> reader,
      final RepositoryItemWriter<UserEntity> writer,
      final PlatformTransactionManager transactionManager,
      final UserItemProcessor userItemProcessor,
      final UserItemWriteListener userItemWriteListener,
      final UseItemReadListener userItemReadListener
  ) {
    return new StepBuilder(USER_JOB_STEP_NAME, jobRepository)
        .<User, UserEntity>chunk(CHUNK_SIZE, transactionManager)
        .processor(userItemProcessor)
        .listener(userItemWriteListener)
        .listener(userItemReadListener)
        .reader(reader)
        .writer(writer)
        .build();
  }

  /**
   * Creates a user job with the given parameters.
   *
   * @param jobRepository    The JobRepository used for managing job metadata and status.
   * @param userJobListener  The JobExecutionListener used for monitoring job execution.
   * @param userJobStep      The Step representing the user job step.
   * @return The created Job object.
   */
  @Bean
  public Job userJob(
      final JobRepository jobRepository,
      final JobExecutionListener userJobListener,
      final Step userJobStep
  ) {
    final String jobName = USER_JOB_NAME.concat(DELIMITER).concat(UUID.randomUUID().toString());
    return new JobBuilder(jobName, jobRepository)
        .listener(userJobListener)
        .start(userJobStep)
        .build();
  }
}
