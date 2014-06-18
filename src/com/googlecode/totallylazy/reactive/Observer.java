package com.googlecode.totallylazy.reactive;

import java.util.function.Consumer;

public interface Observer<T> {
    void next(T value);

    void error(Throwable error);

    void complete();

    static <T> Observer<T> create(Consumer<T> consumer, Observer<?> observer){
        return new Observer<T>() {
            @Override
            public void next(T value) {
                consumer.accept(value);
            }

            @Override
            public void error(Throwable error) {
                observer.error(error);
            }

            @Override
            public void complete() {
                observer.complete();
            }
        };
    }
}
