package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Block;

public interface Observer<T> {
    void next(T value);

    void error(Throwable error);

    void complete();

    static <T> Observer<T> create(Block<T> block, Observer<?> observer) {
        return new ErrorObserver<>(block, observer);
    }
}
