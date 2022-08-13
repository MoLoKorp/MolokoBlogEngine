package com.moloko.molokoblogengine.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.util.StreamUtils;

class ShellTest {
  @Test
  void testExec() throws IOException {
    Process p = new Shell().exec(new String[] {"echo", "test"});

    assertEquals("test\n", StreamUtils.copyToString(p.getInputStream(), StandardCharsets.UTF_8));
  }
}
