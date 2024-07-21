package com.andersen.userservice.config.batch;

import com.andersen.userservice.entity.user.UserEntity;
import com.andersen.userservice.service.UserWriteErrorCollectorService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserItemWriteListener is a class that implements the ItemWriteListener interface and acts as a
 * listener for write events of UserEntity objects. It is responsible for handling write errors and
 * collecting the details of the errors.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserItemWriteListener implements ItemWriteListener<UserEntity> {

  private final UserWriteErrorCollectorService userWriteErrorCollectorService;

  /**
   * Handles write error events for UserEntity objects.
   * This method logs the exception message and error stack trace,
   * and then collects the exception using a UserWriteErrorCollectorService.
   *
   * @param exception The exception that occurred during the write operation.
   * @param items The chunk of UserEntity items being written.
   */
  @Override
  @Transactional
  public void onWriteError(
      final @NonNull Exception exception,
      final @NonNull Chunk<? extends UserEntity> items
  ) {
    log.error(exception.getMessage(), exception);
    userWriteErrorCollectorService.collect(exception);
  }
}
