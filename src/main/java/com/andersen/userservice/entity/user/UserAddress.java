package com.andersen.userservice.entity.user;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class UserAddress {

  private String street;
  private String city;
  private String state;
  private String zip;
}
