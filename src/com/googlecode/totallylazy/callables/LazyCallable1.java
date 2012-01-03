package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function1;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.synchronizedMap;

public final class LazyCallable1<T, R> extends Function1<T,R> {
    private final Callable1<T,R> callable;
    private final Map<T,R> state = synchronizedMap(new HashMap<T, R>());

    private LazyCallable1(Callable1<T,R> callable) {
        this.callable = callable;
    }

    public static <T,R> Function1<T,R> lazy(Callable1<T,R> callable) {
        return new LazyCallable1<T,R>(callable);
    }

    public final R call(T instance) throws Exception {
        synchronized (state) {
            if (!state.containsKey(instance)) {
                state.put(instance, callable.call(instance));
            }
            return state.get(instance);
        }
    }
}
