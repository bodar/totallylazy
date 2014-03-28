package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Memory;

import java.util.HashMap;
import java.util.Map;

public final class LazyFunction<T, R> implements Function1<T, R>, Memory {
    private final Function1<? super T, ? extends R> callable;
    private final Map<T, R> state = new HashMap<T, R>();
    private final Object lock = new Object();

    private LazyFunction(Function1<? super T, ? extends R> callable) {
        this.callable = callable;
    }

    public static <T, R> LazyFunction<T, R> lazy(Function1<? super T, ? extends R> callable) {
        return new LazyFunction<>(callable);
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
