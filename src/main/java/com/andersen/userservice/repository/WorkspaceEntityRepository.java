package com.andersen.userservice.repository;

import com.andersen.userservice.entity.workspace.WorkspaceEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceEntityRepository extends JpaRepository<WorkspaceEntity, Long> {

  Optional<WorkspaceEntity> findByName(String name);
}
