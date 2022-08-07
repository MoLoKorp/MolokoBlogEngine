package com.moloko.molokoblogengine.util;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Shell {
    public Process exec(String[] command) throws IOException {
        return Runtime.getRuntime().exec(command);
    }
}
