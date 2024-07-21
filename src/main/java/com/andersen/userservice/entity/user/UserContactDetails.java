package com.andersen.userservice.entity.user;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
@EqualsAndHashCode
public class UserContactDetails {

  private String phoneNumber;
  private String email;
}
