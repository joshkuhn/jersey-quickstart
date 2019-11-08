/* Copyright 2019, Joshua M. Kuhn.  All Rights Reserved. */

package com.kuhnjosh.handlers;

import org.junit.jupiter.api.Test;

class TestPingHandler {

    @Test
    public void testPing() {
        PingHandler handler = new PingHandler();
        String result = handler.ping();
        assert(result.equals("OK"));
    }

}
