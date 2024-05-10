package com.andersen.userservice.config.batch;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import com.andersen.userservice.entity.user.UserAddress;
import com.andersen.userservice.entity.user.UserContactDetails;
import com.andersen.userservice.entity.user.UserDetails;
import com.andersen.userservice.entity.user.UserEntity;
import com.andersen.userservice.entity.workspace.WorkspaceEntity;
import com.andersen.userservice.model.user.User;
import com.andersen.userservice.model.workspace.Workspace;
import com.andersen.userservice.repository.UserEntityRepository;
import com.andersen.userservice.repository.WorkspaceEntityRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserItemProcessor implements ItemProcessor<User, UserEntity> {

  private final UserEntityRepository userEntityRepository;
  private final WorkspaceEntityRepository workspaceEntityRepository;

  @Override
  @Transactional
  public UserEntity process(final @NonNull User item) {
    final UserEntity userEntity = new UserEntity();

    final Long userIdentifier = getUserIdentifier(item);
    if (Objects.isNull(userIdentifier)) {
      userEntity.setUserAddress(getUserAddress(item));
      userEntity.setUserDetails(getUserDetails(item));
      userEntity.setUserContactDetails(getUserContactDetails(item));
      addWorkspaces(item, userEntity);
    } else {
      final var existingUser = userEntityRepository.findById(userIdentifier).orElseThrow();
      addWorkspaces(item, existingUser);
    }
    return userEntity;
  }

  private Long getUserIdentifier(final User item) {
    final Optional<UserEntity> optUser = userEntityRepository.findByEmail(item.getEmail());
    if (optUser.isPresent()) {
      final UserEntity userEntity = optUser.get();
      return userEntity.getId();
    }
    return null;
  }

  private void addWorkspaces(final User item, final UserEntity userEntity) {
    final List<Workspace> workspaces = item.getWorkspaces();
    if (isEmpty(workspaces)) {
      return;
    }

    workspaces.forEach(workspace -> {
      final var workspaceEntityOpt = workspaceEntityRepository.findByName(workspace.getName());
      if (workspaceEntityOpt.isPresent()) {
        final WorkspaceEntity workspaceEntity = workspaceEntityOpt.get();
        userEntity.addWorkspace(workspaceEntity);
      } else {
        final WorkspaceEntity workspaceEntity = new WorkspaceEntity();
        workspaceEntity.setName(workspace.getName());
        workspaceEntity.setDescription(workspace.getDescription());
        workspaceEntity.setCreationDate(workspaceEntity.getCreationDate());
        userEntity.addWorkspace(workspaceEntity);
      }
    });
  }

  private UserContactDetails getUserContactDetails(final User item) {
    final UserContactDetails userContactDetails = new UserContactDetails();
    userContactDetails.setEmail(item.getEmail());
    userContactDetails.setPhoneNumber(item.getPhoneNumber());
    return userContactDetails;
  }

  private UserDetails getUserDetails(final User item) {
    final UserDetails userDetails = new UserDetails();
    userDetails.setUsername(item.getUsername());
    userDetails.setLastName(item.getLastName());
    userDetails.setFirstName(item.getFirstName());
    userDetails.setAge(item.getAge());
    return userDetails;
  }

  private UserAddress getUserAddress(final User item) {
    final UserAddress userAddress = new UserAddress();
    userAddress.setCity(item.getCity());
    userAddress.setState(item.getState());
    userAddress.setZip(item.getZip());
    userAddress.setStreet(item.getStreet());
    return userAddress;
  }
}
