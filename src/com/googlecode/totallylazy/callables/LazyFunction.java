package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Memory;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Value;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Sequences.sequence;

public final class LazyFunction<T, R> implements Function<T, R>, Memory, Value<Sequence<R>> {
    private final Function<? super T, ? extends R> callable;
    private final Map<T, R> state = new HashMap<T, R>();
    private final Object lock = new Object();

    private LazyFunction(Function<? super T, ? extends R> callable) {
        this.callable = callable;
    }

    public static <T, R> LazyFunction<T, R> lazy(Function<? super T, ? extends R> callable) {
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

    @Override
    public Sequence<R> value() {
        return sequence(state.values());
    }
}
