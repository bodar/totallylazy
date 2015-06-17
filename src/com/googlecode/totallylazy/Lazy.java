package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.LazyCallable;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Closeables.safeClose;

public abstract class Lazy<T> implements Function0<T>, Memory {
    private final Object lock = new Object();
    private volatile Either<Exception, T> state;

    protected abstract T get() throws Exception;

    public static <T> Lazy<T> lazy(Callable<? extends T> callable) {
        return LazyCallable.lazy(callable);
    }

    // Thread-safe double check idiom (Effective Java 2nd edition p.283)
    public final T call() throws Exception {
        if (state == null) {
            synchronized (lock) {
                if (state == null) {
                    try {
                        state = Either.right(get());
                    } catch (Exception e) {
                        state = Either.left(e);
                    }
                }
            }
        }
        if(state.isLeft()){
            throw state.left();
        }
        return state.right();
    }

    public void forget() {
        close();
    }

    @Override
    public void close() {
        synchronized (lock) {
            if(state != null) {
                safeClose(state.value());
                state = null;
            }
        }
    }
}
