package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Pair;

import java.util.Map;

public interface MapFactory<K, V, M extends PersistentMap<K, V>> {
    M empty();

    M empty(Class<K> kClass, Class<V> vClass);

    M map();

    M map(K key, V value);

    M map(K key1, V value1, K key2, V value2);

    M map(K key1, V value1, K key2, V value2, K key3, V value3);

    M map(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4);

    M map(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5);

    @SuppressWarnings("unchecked")
    M map(final Pair<K, V> head, final Pair<K, V>... tail);

    M map(final Iterable<? extends Pair<K, V>> values);

    M map(final Map<K, V> values);
}
