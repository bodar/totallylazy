package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Block;

import java.util.concurrent.atomic.AtomicBoolean;

public class ErrorObserver<T> implements Observer<T> {
    private final Block<T> block;
    private final Observer<?> observer;
    private final AtomicBoolean error = new AtomicBoolean(false);

    public ErrorObserver(Block<T> block, Observer<?> observer) {
        this.block = block;
        this.observer = observer;
    }

    @Override
    public void next(T value) {
        if (error.get()) return;
        try {
            block.call(value);
        } catch (Exception e) {
            error.set(true);
            observer.error(e);
        }
    }

    @Override
    public void error(Throwable throwable) {
        if (error.get()) return;
        observer.error(throwable);
    }

    @Override
    public void complete() {
        if (error.get()) return;
        observer.complete();
    }
}
