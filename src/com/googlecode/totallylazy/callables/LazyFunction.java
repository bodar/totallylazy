package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Memory;

import java.util.HashMap;
import java.util.Map;

public final class LazyFunction<T, R> extends com.googlecode.totallylazy.Eq implements Memory, Function<T,R> {
    private final Function<? super T, ? extends R> callable;
    private final Map<T, R> state = new HashMap<T, R>();
    private final Object lock = new Object();

    private LazyFunction(Function<? super T, ? extends R> callable) {
        this.callable = callable;
    }

    public static <T, R> LazyFunction<T, R> lazy(Function<? super T, ? extends R> callable) {
        return new LazyFunction<T, R>(callable);
    }

    public final R call(T instance) throws Exception {
        synchronized (lock) {
            if (!state.containsKey(instance)) {
                state.put(instance, callable.call(instance));
            }
            return state.get(instance);
        }
    }

    public void forget() {
        synchronized (lock) {
            state.clear();
        }
    }
}
