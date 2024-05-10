package com.andersen.userservice.entity.user;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class UserContactDetails {

  private String phoneNumber;
  private String email;
}
