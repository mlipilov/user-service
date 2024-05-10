package com.andersen.userservice.repository;

import com.andersen.userservice.entity.user.err.UserWriteErrorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWriteErrorEntityRepository extends JpaRepository<UserWriteErrorEntity, Long> {

}