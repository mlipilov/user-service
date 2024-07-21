package com.andersen.userservice.service;

/**
 * UserWriteErrorCollectorService is an interface that defines the contract for a service that
 * collects user write errors. This service is responsible for collecting the details of user write
 * errors, such as exceptions, and storing them in a repository.
 */
public interface UserWriteErrorCollectorService {

  /**
   * Collects the provided exception and stores it in a repository for user write errors.
   *
   * @param exception The exception to be collected and stored.
   */
  void collect(Exception exception);
}
