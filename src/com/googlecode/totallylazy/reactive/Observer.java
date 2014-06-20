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

    public static <T> Observer<T> observer(Block<? super T> next, Block<? super Throwable> error, Runnable complete) {
        return new BaseObserver<T>(next, error, complete);
    }

    public static <T> Observer<T> observer(Block<? super T> block, Observer<?> observer) {
        return observer(block, observer::error, observer::complete);
    }

    public static <T> Observer<T> observer(Observer<T> observer) {
        return observer(observer::next, observer);
    }
}
