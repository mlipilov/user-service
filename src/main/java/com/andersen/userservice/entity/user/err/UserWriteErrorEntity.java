package com.andersen.userservice.entity.user.err;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "user_write_errors")
public class UserWriteErrorEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  private LocalDateTime createdAt;
  private String stackTrace;
  private String message;
}
