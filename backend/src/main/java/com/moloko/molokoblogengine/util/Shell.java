package com.moloko.molokoblogengine.util;

import java.io.IOException;
import org.springframework.stereotype.Component;

/** Runtime class wrapper created to mitigate issues with Runtime static methods mocking. */
@Component
public class Shell {
  public Process exec(String[] command) throws IOException {
    return Runtime.getRuntime().exec(command);
  }
}
