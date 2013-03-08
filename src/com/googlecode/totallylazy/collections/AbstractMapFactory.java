package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequences;

import java.util.Map;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;

public abstract class AbstractMapFactory<K, V, M extends PersistentMap<K, V>> implements MapFactory<K,V,M> {
    @Override
    public M empty(Class<K> kClass, Class<V> vClass) {
        return empty();
    }

    @Override
    public M empty() {
        return map(Sequences.<Pair<K,V>>empty());
    }

    @Override
    public M map() {
        return empty();
    }

    @Override
    public M map(K key, V value) {
        return map(sequence(pair(key, value)));
    }

    @Override
    public M map(K key1, V value1, K key2, V value2) {
        return map(sequence(pair(key1, value1), pair(key2, value2)));
    }

    public M map(K key1, V value1, K key2, V value2, K key3, V value3) {
        return map(sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3)));
    }

    public M map(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
        return map(sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3), pair(key4, value4)));
    }

    public M map(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
        return map(sequence(pair(key1, value1), pair(key2, value2), pair(key3, value3), pair(key4, value4), pair(key5, value5)));
    }

    public M map(final Pair<K, V> head, final Pair<K, V>... tail) {
        return map(sequence(tail).cons(head));
    }

    @Override
    public M map(Map<K, V> values) {
        return map(Maps.pairs(values));
    }
}
