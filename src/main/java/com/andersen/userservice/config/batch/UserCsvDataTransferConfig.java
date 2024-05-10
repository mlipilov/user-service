package com.andersen.userservice.config.batch;

import static java.lang.System.currentTimeMillis;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

import com.andersen.userservice.entity.user.UserEntity;
import com.andersen.userservice.model.user.User;
import com.andersen.userservice.repository.UserEntityRepository;
import java.util.Properties;
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

@Configuration
public class UserCsvDataTransferConfig {

  private static final int CHUNK_SIZE = 100;

  private static final String USER_JOB_NAME = "transferCsvUserDataJob";
  private static final String USER_JOB_STEP_NAME = "transferCsvUserDataStep";
  private static final String USER_KAFKA_ITEM_READER_NAME = "UserKafkaItemReaderName";
  public static final String DELIMITER = "_";

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

  @Bean
  public RepositoryItemWriter<UserEntity> userWriter(
      final UserEntityRepository userEntityRepository
  ) {
    return new RepositoryItemWriterBuilder<UserEntity>()
        .repository(userEntityRepository)
        .build();
  }

  @Bean
  public Step userJobStep(
      final JobRepository jobRepository,
      final KafkaItemReader<String, User> reader,
      final RepositoryItemWriter<UserEntity> writer,
      final PlatformTransactionManager platformTransactionManager,
      final UserItemProcessor userItemProcessor,
      final UserItemWriteListener userItemWriteListener,
      final UseItemReadListener userItemReadListener
  ) {
    return new StepBuilder(USER_JOB_STEP_NAME, jobRepository)
        .<User, UserEntity>chunk(CHUNK_SIZE, platformTransactionManager)
        .processor(userItemProcessor)
        .listener(userItemWriteListener)
        .listener(userItemReadListener)
        .reader(reader)
        .writer(writer)
        .build();
  }

  @Bean
  public Job userJob(
      final JobRepository jobRepository,
      final JobExecutionListener userJobListener,
      final Step userJobStep
  ) {
    final String jobName = USER_JOB_NAME.concat(DELIMITER) + currentTimeMillis();
    return new JobBuilder(jobName, jobRepository)
        .listener(userJobListener)
        .start(userJobStep)
        .build();
  }
}
