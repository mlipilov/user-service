package com.andersen.userservice.config.ta;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Configuration class for defining the transaction manager bean.
 */
@Configuration
public class TransactionConfig {

  /**
   * Configures and returns the transaction manager bean for handling transactions.
   *
   * @param entityManagerFactory The entity manager factory for creating entity managers.
   * @return The configured platform transaction manager.
   */
  @Bean
  @Primary
  public PlatformTransactionManager transactionManager(
      final EntityManagerFactory entityManagerFactory
  ) {
    final JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory);
    return transactionManager;
  }
}
