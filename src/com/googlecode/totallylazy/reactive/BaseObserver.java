package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Block;

import java.util.concurrent.atomic.AtomicBoolean;

class BaseObserver<T> implements Observer<T> {
    private final Block<? super T> next;
    private final Block<? super Throwable> error;
    private final Runnable complete;
    private final AtomicBoolean thrown = new AtomicBoolean(false);

    BaseObserver(Block<? super T> next, Block<? super Throwable> error, Runnable complete) {
        this.next = next;
        this.error = error;
        this.complete = complete;
    }

    @Override
    public void next(T value) {
        if (thrown.get()) return;
        try {
            next.call(value);
        } catch (Exception e) {
            error(e);
        }
    }

    @Override
    public void error(Throwable throwable) {
        if (thrown.get()) return;
        thrown.set(true);
        error.accept(throwable);
    }

    @Override
    public void complete() {
        if (thrown.get()) return;
        complete.run();
    }
}
