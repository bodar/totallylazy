package com.googlecode.totallylazy.reactive;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.googlecode.totallylazy.reactive.State.Continue;
import static com.googlecode.totallylazy.reactive.State.Stop;

public class ListSubject<T> implements Subject<T> {
    protected final List<Observer<T>> observers = new CopyOnWriteArrayList<>();

    @Override
    public AutoCloseable subscribe(Observer<T> observer) {
        observers.add(observer);
        return EMPTY_CLOSEABLE;
    }

    @Override
    public State start() {
        for (Observer<T> observer : observers) {
            if (observer.start().equals(Stop)) observers.remove(observer);
        }
        return observers.isEmpty() ? Stop : Continue;
    }

    @Override
    public State next(T item) {
        for (Observer<T> observer : observers) {
            if (observer.next(item).equals(Stop)) observers.remove(observer);
        }
        return observers.isEmpty() ? Stop : Continue;
    }

    @Override
    public void error(Throwable throwable) {
        for (Observer<T> observer : observers) {
            observer.error(throwable);
        }
    }

    @Override
    public void finish() {
        for (Observer<T> observer : observers) {
            observer.finish();
        }
    }
}
