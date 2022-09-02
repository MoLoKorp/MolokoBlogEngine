package com.moloko.molokoblogengine;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MolokoBlogEngineApplicationTests {

  static {
    System.setProperty("MONGO_PASSWORD", "mongostub");
  }

  @Test
  void contextLoads() {}
}
