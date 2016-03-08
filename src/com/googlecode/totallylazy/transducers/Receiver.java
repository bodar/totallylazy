package com.googlecode.totallylazy.transducers;


import com.googlecode.totallylazy.functions.Function0;
import com.googlecode.totallylazy.functions.Function1;

public interface Receiver<T> {
    State start();

    State next(T item);

    void error(Throwable throwable);

    void finish();

    static <T> Receiver<T> receiver(Receiver<?> delegate, Function1<T, State> function) {
        return receiver(delegate, delegate::start, function);
    }

    static <T> Receiver<T> receiver(Receiver<?> delegate, Function0<State> start, Function1<T, State> function) {
        return receiver(delegate, start, function, delegate::finish);
    }

    static <T> Receiver<T> receiver(Receiver<?> delegate, Function1<T, State> function, Runnable finished) {
        return receiver(delegate, delegate::start, function, finished);
    }

    static <T> Receiver<T> receiver(Receiver<?> delegate, Function0<State> start, Function1<T, State> function, Runnable finished) {
        return new Delegate<T>() {
            @Override
            public Receiver<?> delegate() {
                return delegate;
            }

            @Override
            public State start() {
                return start.apply();
            }

            @Override
            public State next(T item) {
                return function.apply(item);
            }

            @Override
            public void finish() {
                finished.run();
            }
        };
    }

    interface Delegate<T> extends Receiver<T> {
        Receiver<?> delegate();

        @Override
        default State start() {
            return delegate().start();
        }

        @Override
        default void error(Throwable throwable) {
            delegate().error(throwable);
        }

        @Override
        default void finish() {
            delegate().finish();
        }
    }
}
