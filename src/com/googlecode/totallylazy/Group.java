package com.googlecode.totallylazy;

import java.util.Iterator;

import static com.googlecode.totallylazy.Unchecked.cast;

public class Group<K, V> extends Sequence<V> {
    private final K key;
    private final Iterable<? extends V> values;

    public Group(K key, Iterable<? extends V> values) {
        this.key = key;
        this.values = values;
    }

    public K key() {
        return key;
    }

    public Iterator<V> iterator() {
        return cast(values.iterator());
    }

    public static class functions {
        public static <K, V> Function1<Group<K, V>, K> key() {
            return group -> group.key();
        }
    }
}
