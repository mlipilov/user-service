package com.andersen.userservice.repository;

import com.andersen.userservice.entity.user.UserEntity;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

  @Query("""
      SELECT u
      FROM UserEntity u
      WHERE u.userContactDetails.email = :email
      """)
  Optional<UserEntity> findByEmail(@NonNull @Param(value = "email") String email);
}