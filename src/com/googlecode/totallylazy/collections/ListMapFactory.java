package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Pair;

import static com.googlecode.totallylazy.Unchecked.cast;

public class ListMapFactory<K,V> extends AbstractMapFactory<K,V, ListMap<K,V>> {
    @Override
    public ListMap<K, V> empty() {
        return cast(ListMap.emptyListMap());
    }

    @Override
    public ListMap<K, V> map(Iterable<? extends Pair<K, V>> values) {
        return cast(ListMap.listMap(values));
    }
}