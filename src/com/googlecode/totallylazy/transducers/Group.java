package com.googlecode.totallylazy.transducers;

import com.googlecode.totallylazy.Key;

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
