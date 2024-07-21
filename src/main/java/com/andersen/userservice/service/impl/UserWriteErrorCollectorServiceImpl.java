package com.andersen.userservice.service.impl;

import com.andersen.userservice.entity.user.err.UserWriteErrorEntity;
import com.andersen.userservice.repository.UserWriteErrorEntityRepository;
import com.andersen.userservice.service.UserWriteErrorCollectorService;
import java.time.LocalDateTime;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserWriteErrorCollectorServiceImpl implements UserWriteErrorCollectorService {

  private final UserWriteErrorEntityRepository userWriteErrorEntityRepository;

  @Override
  @Transactional(transactionManager = "transactionManager")
  public void collect(final Exception exception) {
    log.warn("Started collecting error...");
    final UserWriteErrorEntity userWriteErrorEntity = new UserWriteErrorEntity();
    userWriteErrorEntity.setMessage(exception.getMessage());
    userWriteErrorEntity.setCreatedAt(LocalDateTime.now());
    userWriteErrorEntity.setStackTrace(Arrays.toString(exception.getStackTrace()));
    userWriteErrorEntityRepository.save(userWriteErrorEntity);
  }
}
