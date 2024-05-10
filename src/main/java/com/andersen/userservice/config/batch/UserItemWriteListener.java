package com.andersen.userservice.config.batch;

import com.andersen.userservice.entity.user.UserEntity;
import com.andersen.userservice.service.UserWriteErrorCollectorService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserItemWriteListener implements ItemWriteListener<UserEntity> {

  private final UserWriteErrorCollectorService userWriteErrorCollectorService;

  @Override
  public void onWriteError(
      final @NonNull Exception exception,
      final @NonNull Chunk<? extends UserEntity> items
  ) {
    log.error(exception.getMessage(), exception);
    userWriteErrorCollectorService.collect(exception);
  }
}
