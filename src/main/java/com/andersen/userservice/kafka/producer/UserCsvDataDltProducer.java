package com.andersen.userservice.kafka.producer;

/**
 * This interface represents a user CSV data dead-letter (DLT) producer. It provides a method to
 * produce an exception to a DLT topic.
 */
public interface UserCsvDataDltProducer {

  /**
   * This method is used to produce an exception to the dead-letter (DLT) topic.
   *
   * @param ex The exception to be produced to the DLT topic.
   */
  void produce(Exception ex);
}
