package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Callable1;

import java.util.HashMap;
import java.util.Map;

public final class CountingCallable1<T,R> implements Callable1<T,R> {
    private final Map<T, Integer> count = new HashMap<T, Integer>();
    private final Callable1<T, R> callable;

    private CountingCallable1(Callable1<T,R> callable) {
        this.callable = callable;
    }

    public final R call(T t) throws Exception {
        count.put(t, count(t) + 1);
        return callable.call(t);
    }

    public final int count(T t) {
        if(!count.containsKey(t)){
            count.put(t, 0);
        }
        return count.get(t);
    }

    public static <T,R> CountingCallable1<T,R> counting(Callable1<T,R> callable) {
        return new CountingCallable1<T,R>(callable);
    }
}
