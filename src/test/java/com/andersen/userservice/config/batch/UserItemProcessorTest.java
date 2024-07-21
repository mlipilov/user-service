package com.andersen.userservice.config.batch;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.andersen.userservice.entity.user.UserEntity;
import com.andersen.userservice.model.user.User;
import com.andersen.userservice.model.workspace.Workspace;
import com.andersen.userservice.repository.UserEntityRepository;
import com.andersen.userservice.repository.WorkspaceEntityRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * UserItemProcessorTest class tests the process method of the UserItemProcessor class.
 */
@ExtendWith(MockitoExtension.class)
public class UserItemProcessorTest {

  private static final String USER_EMAIL = "email@mail.com";
  private static final String WORKSPACE = "workspace";

  @Mock
  private UserEntityRepository userEntityRepository;
  @Mock
  private WorkspaceEntityRepository workspaceEntityRepository;

  @InjectMocks
  private UserItemProcessor userItemProcessor;

  @Test
  public void shouldProcessUserItemTest() {
    final User user = new User();
    user.setEmail(USER_EMAIL);
    final Workspace workspace = new Workspace();
    workspace.setName(WORKSPACE);
    user.setWorkspaces(List.of(workspace));

    // Testing condition when UserEntity does not exist for the given user email
    when(userEntityRepository.findByEmail(anyString())).thenReturn(Optional.empty());

    final UserEntity processedUserEntity = userItemProcessor.process(user);

    assertEquals(user.getEmail(), processedUserEntity.getUserContactDetails().getEmail());
    verify(userEntityRepository, times(1)).findByEmail(user.getEmail());
    verify(workspaceEntityRepository, times(user.getWorkspaces().size())).findByName(anyString());
  }

  @Test
  public void shouldAttachWorkspacesToExistingUserEntityTest() {
    final User user = new User();
    user.setEmail(USER_EMAIL);
    final Workspace workspace = new Workspace();
    workspace.setName(WORKSPACE);
    user.setWorkspaces(List.of(workspace));

    // Testing condition when UserEntity exists for the given user email
    final UserEntity userEntity = new UserEntity();
    when(userEntityRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));

    final UserEntity processedUserEntity = userItemProcessor.process(user);

    assertEquals(
        user.getWorkspaces().get(INTEGER_ZERO).getName(),
        processedUserEntity.getWorkspaces().iterator().next().getName());
    verify(userEntityRepository, times(1)).findByEmail(user.getEmail());
    verify(workspaceEntityRepository, times(user.getWorkspaces().size())).findByName(anyString());
  }
}