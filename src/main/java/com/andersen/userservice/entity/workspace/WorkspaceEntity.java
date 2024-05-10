package com.andersen.userservice.entity.workspace;

import static jakarta.persistence.GenerationType.IDENTITY;

import com.andersen.userservice.entity.user.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "workspaces")
public class WorkspaceEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  private String name;
  private LocalDate creationDate;
  private String description;

  @ManyToOne
  private UserEntity user;
}
