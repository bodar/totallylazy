package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Key;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Group<K, T> extends ListSubject<T> implements Key<K> {
    private final K key;

    public Group(K key) {
        this.key = key;
    }

    @Override
    public K key() {
        return key;
    }
}
