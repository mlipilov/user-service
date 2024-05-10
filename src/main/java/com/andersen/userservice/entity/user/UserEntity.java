package com.andersen.userservice.entity.user;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.util.Collections.unmodifiableSet;
import static lombok.AccessLevel.NONE;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import com.andersen.userservice.entity.workspace.WorkspaceEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  @Embedded
  private UserDetails userDetails;
  @Embedded
  private UserAddress userAddress;
  @Embedded
  private UserContactDetails userContactDetails;

  @Setter(NONE)
  @OneToMany(mappedBy = "user", cascade = {PERSIST})
  private Set<WorkspaceEntity> workspaces = new HashSet<>();

  /**
   * ATTN! Read-only getter, change the state through {@link #addWorkspace(WorkspaceEntity)} or
   * {@link #addWorkspaces(Set)}
   *
   * @return List.of {@link WorkspaceEntity}
   */
  public Set<WorkspaceEntity> getWorkspaces() {
    return unmodifiableSet(this.workspaces);
  }

  public void addWorkspaces(final @NonNull Set<WorkspaceEntity> workspaces) {
    if (isEmpty(workspaces)) {
      throw new IllegalArgumentException("Workspaces cannot be empty");
    }

    workspaces.forEach(workspace -> workspace.setUser(this));
    this.workspaces.addAll(workspaces);
  }

  public void addWorkspace(final @NonNull WorkspaceEntity workspace) {
    workspace.setUser(this);
    this.workspaces.add(workspace);
  }
}
