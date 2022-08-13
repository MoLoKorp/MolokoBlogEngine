package com.moloko.molokoblogengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * MolokoBlogEngine SpringBoot application build on top of reactive stack (WebFlux + reactive
 * MongoDB).
 */
@EnableReactiveMongoRepositories
@SpringBootApplication
public class MolokoBlogEngineApplication {

  public static void main(String[] args) {
    SpringApplication.run(MolokoBlogEngineApplication.class, args);
  }
}
