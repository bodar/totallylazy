package com.googlecode.totallylazy;

import java.util.Iterator;

public class Group<K, V> extends Sequence<V>{
    private final K key;
    private final Iterable<V> values;

    public Group(K key, Iterable<V> values) {
        this.key = key;
        this.values = values;
    }

    public K key() {
        return key;
    }

    public Iterator<V> iterator() {
        return values.iterator();
    }
}
