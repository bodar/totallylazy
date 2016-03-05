package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Returns;

public interface Observer<T> {
    State start();

    State next(T item);

    void error(Throwable throwable);

    void finish();

    static <T> Observer<T> observer(Observer<?> delegate, Function<T, State> function) {
        return observer(delegate, delegate::start, function);
    }

    static <T> Observer<T> observer(Observer<?> delegate, Returns<State> start, Function<T, State> function) {
        return observer(delegate, start, function, delegate::finish);
    }

    static <T> Observer<T> observer(Observer<?> delegate, Function<T, State> function, Runnable finished) {
        return observer(delegate, delegate::start, function, finished);
    }

    static <T> Observer<T> observer(Observer<?> delegate, Returns<State> start, Function<T, State> function, Runnable finished) {
        return new Delegate<T>() {
            @Override
            public Observer<?> delegate() {
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

    interface Delegate<T> extends Observer<T> {
        Observer<?> delegate();

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
