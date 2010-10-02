package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Option;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;

public class LazyCallable<T> implements Callable<T> {
    private final Callable<T> callable;
    private Option<T> state = none();

    private LazyCallable(Callable<T> callable) {
        this.callable = callable;
    }

    public static <T> Callable<T> lazy(Callable<T> callable) {
        return new LazyCallable<T>(callable);
    }

    public T call() throws Exception {
        synchronized (state) {
            if (state.isEmpty()) {
                state = some(callable.call());
            }
            return state.get();
        }
    }
}
