package com.googlecode.totallylazy.callables;

import java.util.concurrent.Callable;

public final class CountingCallable implements Callable<Integer> {
    private int count = 0;

    private CountingCallable() {
    }

    public final Integer call() throws Exception {
        return count++;
    }

    public final int count() {
        return count;
    }

    public static CountingCallable counting() {
        return new CountingCallable();
    }
}
