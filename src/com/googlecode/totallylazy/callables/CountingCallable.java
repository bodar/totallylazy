package com.googlecode.totallylazy.callables;

import java.util.concurrent.Callable;

public class CountingCallable implements Callable<Integer> {
    private int count = 0;

    private CountingCallable() {
    }

    public Integer call() throws Exception {
        return count++;
    }

    public int count() {
        return count;
    }

    public static CountingCallable counting() {
        return new CountingCallable();
    }
}
