package com.googlecode.totallylazy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callers.callConcurrently;
import static java.lang.Thread.currentThread;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

public class CallersTest {
    @Test
    public void callConcurrentlyIgnoresAnEmptyIterator() throws Exception {
        callConcurrently(new ArrayList<Callable<Object>>());
    }

    @Test
    public void callConcurrentlyRunsOnDifferentThreads() {
        Sequence<String> sequence = callConcurrently(currentThreadName(), currentThreadName());
        assertThat(sequence.first(), is(not(sequence.second())));
    }

    private static Function<String> currentThreadName() {
        return new Function<String>() {
            @Override
            public String call() throws Exception {
                return currentThread().getName();
            }
        };
    }
}
