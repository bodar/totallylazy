package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Pair;

public class HashTreeMapFactory<K, V> extends AbstractMapFactory<K, V, HashTreeMap<K, V>> {
    @Override
    public HashTreeMap<K, V> map(Iterable<? extends Pair<K, V>> values) {
        return HashTreeMap.hashTreeMap(values);
    }
}
