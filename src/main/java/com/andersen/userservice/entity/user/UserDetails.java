package com.andersen.userservice.entity.user;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class UserDetails {

  private String username;
  private String firstName;
  private String lastName;
  private Integer age;
}
