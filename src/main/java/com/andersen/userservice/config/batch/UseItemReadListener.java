package com.andersen.userservice.config.batch;

import com.andersen.userservice.kafka.producer.UserCsvDataDltProducer;
import com.andersen.userservice.model.user.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is an implementation of the ItemReadListener interface. It handles read errors that
 * occur during the item reading process.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UseItemReadListener implements ItemReadListener<User> {

  private final UserCsvDataDltProducer userCsvDataDltProducer;

  /**
   * Handles read errors that occur during the item reading process. This method is called when an exception is thrown during the reading of an item.
   *
   * @param ex The exception that was thrown during the item reading process.
   */
  @Override
  @Transactional
  public void onReadError(@NonNull final Exception ex) {
    log.error(ex.getMessage(), ex);
    userCsvDataDltProducer.produce(ex);
  }
}
