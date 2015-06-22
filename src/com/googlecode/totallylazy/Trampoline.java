package com.googlecode.totallylazy;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callables.compose;
import static com.googlecode.totallylazy.Callers.call;

public abstract class Trampoline<T> implements Functor<T> {
    public static <T> Trampoline<T> done(T value) {
        return new Done<T>(value);
    }

    public static <T> Trampoline<T> more(Callable<? extends Trampoline<T>> function) {
        return new More<T>(function);
    }

    public T trampoline() {
        return trampoline(this);
    }

    public static <T> T trampoline(Trampoline<? extends T> trampoline) {
        while (trampoline instanceof More) {
            trampoline = call(Unchecked.<More<T>>cast(trampoline));
        }
        return Unchecked.<Done<T>>cast(trampoline).value();
    }

    private static class Done<T> extends Trampoline<T> implements Value<T> {
        private final T value;

        public Done(T value) {
            this.value = value;
        }

        @Override
        public T value() {
            return value;
        }

        @Override
        public <S> Trampoline<S> map(Function1<? super T, ? extends S> callable) {
            return done(call(callable, value));
        }
    }

    private static class More<T> extends Trampoline<T> implements Callable<Trampoline<T>> {
        private final Callable<? extends Trampoline<T>> callable;

        public More(Callable<? extends Trampoline<T>> callable) {
            this.callable = callable;
        }

        public Trampoline<T> call() throws Exception {
            return callable.call();
        }

        @Override
        public <S> Trampoline<S> map(final Function1<? super T, ? extends S> callable) {
            return more(compose(this.callable, trampoline -> {
                return null; //callable.call(trampoline);
            }));
        }
    }
}
