package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Key;
import com.googlecode.totallylazy.Sequence;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Group<K,T> implements Observable<T>, Key<K> {
    private final K key;
    private final List<Observer<T>> observers = new CopyOnWriteArrayList<>();

    public Group(K key) {
        this.key = key;
    }

    void observe(T t) {
        for (Observer<T> observer : observers) observer.next(t);
    }

    @Override
    public K key() {
        return key;
    }

    @Override
    public AutoCloseable subscribe(Observer<T> observer) {
        observers.add(observer);
        return EMPTY_CLOSEABLE;
    }

    public Sequence<Observer<T>> observers() {
        return sequence(observers);
    }
}
