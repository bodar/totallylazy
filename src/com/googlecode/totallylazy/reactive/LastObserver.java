package com.googlecode.totallylazy.reactive;

import java.util.concurrent.atomic.AtomicReference;

public class LastObserver<T> implements Observer<T> {
    private final AtomicReference<T> reference = new AtomicReference<>();
    private final Observer<T> observer;

    public LastObserver(Observer<T> observer) {
        this.observer = observer;
    }

    @Override
    public void next(T value) {
        reference.set(value);
    }

    @Override
    public void error(Throwable error) {
        observer.error(error);
    }

    @Override
    public void complete() {
        T t = reference.get();
        if( t != null) observer.next(t);
        observer.complete();
    }
}
