package com.andersen.userservice.kafka.producer.impl;

import com.andersen.userservice.kafka.producer.UserCsvDataDltProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCsvDataDltProducerImpl implements UserCsvDataDltProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Value("${spring.kafka.topics.user-csv-data-dlt-topic}")
  private String dltTopic;

  @Override
  public void produce(final Exception ex) {
    log.info("Started producing to DLT...");
    kafkaTemplate.send(dltTopic, ex.getMessage())
        .whenComplete((msg, err) -> logResult(err));
  }

  private void logResult(final Throwable err) {
    if (err != null) {
      log.error("Error occurred while sending data to DLT", err);
    } else {
      log.info("Successfully produced data to DLT");
    }
  }
}
