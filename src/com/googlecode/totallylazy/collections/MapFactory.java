package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Pair;

import java.util.Map;

public interface MapFactory<K, V, M extends PersistentMap<K, V>> {
    public M empty();

    public M empty(Class<K> kClass, Class<V> vClass);

    public M map();

    public M map(K key, V value);

    public M map(K key1, V value1, K key2, V value2);

    public M map(K key1, V value1, K key2, V value2, K key3, V value3);

    public M map(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4);

    public M map(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5);

    @SuppressWarnings("unchecked")
    public M map(final Pair<K, V> head, final Pair<K, V>... tail);

    public M map(final Iterable<? extends Pair<K, V>> values);

    public M map(final Map<K, V> values);
}
