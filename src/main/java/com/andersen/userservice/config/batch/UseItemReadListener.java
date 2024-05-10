package com.andersen.userservice.config.batch;

import com.andersen.userservice.kafka.producer.UserCsvDataDltProducer;
import com.andersen.userservice.model.user.User;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UseItemReadListener implements ItemReadListener<User> {

  private final UserCsvDataDltProducer userCsvDataDltProducer;

  @Override
  @Transactional
  public void onReadError(@NonNull final Exception ex) {
    log.error(ex.getMessage(), ex);
    userCsvDataDltProducer.produce(ex);
  }
}
