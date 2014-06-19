package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Block;

public interface Observer<T> {
    void next(T value);

    default T nextR(T value) {
        next(value);
        return value;
    }

    void error(Throwable throwable);

    void complete();

    static <T> Observer<T> create(Block<T> block, Observer<?> observer) {
        return new ErrorObserver<>(block, observer);
    }

    static <T> Observer<T> observer(Block<? super T> next, Block<? super Throwable> error, Runnable completed ) {
        return new Observer<T>() {
            @Override
            public void next(T value) {
                next.accept(value);
            }

            @Override
            public void error(Throwable throwable) {
                error.accept(throwable);
            }

            @Override
            public void complete() {
                completed.run();
            }
        };
    }
}
