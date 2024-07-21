package com.andersen.userservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import com.andersen.userservice.entity.user.err.UserWriteErrorEntity;
import com.andersen.userservice.repository.UserWriteErrorEntityRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserWriteErrorCollectorServiceImplTest {

  private static final String EX_MSG = "Test exception";
  private static final String CAUSE_MSG = "Test cause";

  @Mock
  private UserWriteErrorEntityRepository userWriteErrorEntityRepository;

  @InjectMocks
  private UserWriteErrorCollectorServiceImpl userWriteErrorCollectorService;

  @Test
  public void shouldCollectGivenException() {
    // Given
    final Exception givenException = new Exception(EX_MSG, new Throwable(CAUSE_MSG));

    // When
    userWriteErrorCollectorService.collect(givenException);

    // Then
    final var argumentCaptor = ArgumentCaptor.forClass(UserWriteErrorEntity.class);
    verify(userWriteErrorEntityRepository).save(argumentCaptor.capture());
    UserWriteErrorEntity actualUserWriteErrorEntity = argumentCaptor.getValue();

    assertEquals(
        givenException.getMessage(),
        actualUserWriteErrorEntity.getMessage());
    assertEquals(
        Arrays.toString(givenException.getStackTrace()),
        actualUserWriteErrorEntity.getStackTrace());
    assertTrue(actualUserWriteErrorEntity.getCreatedAt()
        .isBefore(LocalDateTime.now().plusSeconds(1)));
    assertTrue(actualUserWriteErrorEntity.getCreatedAt()
        .isAfter(LocalDateTime.now().minusSeconds(1)));
  }
}