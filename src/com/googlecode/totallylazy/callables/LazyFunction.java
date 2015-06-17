package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Memory;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Closeables.safeClose;

public final class LazyFunction<T, R> extends Function<T, R> implements Memory {
    private final Function1<? super T, ? extends R> callable;
    private final Map<T, Either<Exception, R>> state = new HashMap<>();
    private final Object lock = new Object();

    private LazyFunction(Function1<? super T, ? extends R> callable) {
        this.callable = callable;
    }

    public static <T, R> LazyFunction<T, R> lazy(Function1<? super T, ? extends R> callable) {
        return new LazyFunction<>(callable);
    }

    public final R call(T instance) throws Exception{
        synchronized (lock) {
            if (!state.containsKey(instance)) {
                try {
                    state.put(instance, Either.right(callable.call(instance)));
                } catch (Exception e) {
                    state.put(instance, Either.left(e));
                }
            }
            Either<Exception, R> either = state.get(instance);
            if(either.isLeft()) {
                throw either.left();
            }
            return either.right();
        }
    }

    public void forget() {
        close();
    }

    @Override
    public void close() {
        synchronized (lock) {
            for (Either<Exception, R> r : state.values()) {
                if(r != null) safeClose(r.value());
            }
            state.clear();
        }
    }
}
