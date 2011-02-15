package com.googlecode.totallylazy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callers.callConcurrently;

public class CallersTest {
    @Test
    public void callConcurrentlyIgnoresAnEmptyIterator() throws Exception {
        callConcurrently(new ArrayList<Callable<Object>>());

    }
}
