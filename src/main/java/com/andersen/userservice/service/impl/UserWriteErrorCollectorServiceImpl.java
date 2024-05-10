package com.andersen.userservice.service.impl;

import com.andersen.userservice.entity.user.err.UserWriteErrorEntity;
import com.andersen.userservice.repository.UserWriteErrorEntityRepository;
import com.andersen.userservice.service.UserWriteErrorCollectorService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWriteErrorCollectorServiceImpl implements UserWriteErrorCollectorService {

  private final UserWriteErrorEntityRepository userWriteErrorEntityRepository;

  @Override
  @Transactional
  public void collect(final Exception exception) {
    final UserWriteErrorEntity userWriteErrorEntity = new UserWriteErrorEntity();
    userWriteErrorEntity.setMessage(exception.getMessage());
    userWriteErrorEntity.setCreatedAt(LocalDateTime.now());
    userWriteErrorEntity.setStackTrace(Arrays.toString(exception.getStackTrace()));
    userWriteErrorEntityRepository.save(userWriteErrorEntity);
  }
}
