package com.andersen.userservice.service;

public interface UserWriteErrorCollectorService {

  void collect(Exception exception);
}
