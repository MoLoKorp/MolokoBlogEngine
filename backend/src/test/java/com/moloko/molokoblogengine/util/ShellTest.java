package com.moloko.molokoblogengine.util;

import org.junit.jupiter.api.Test;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShellTest {
    @Test
    void testExec() throws IOException {
        Process p = new Shell().exec(new String[]{"echo", "test"});

        assertEquals("test\n", StreamUtils.copyToString(p.getInputStream(), StandardCharsets.UTF_8));
    }
}
