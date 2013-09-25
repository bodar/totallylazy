package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Eq;

import java.util.Map;

public abstract class ReadOnlyMap<K,V> extends Eq implements Map<K,V> {
    @Override
    public V remove(Object o) {
        throw new IllegalMutationException();
    }

    @Override
    public V put(K key, V value) {
        throw new IllegalMutationException();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new IllegalMutationException();
    }

    @Override
    public void clear() {
        throw new IllegalMutationException();
    }

}
