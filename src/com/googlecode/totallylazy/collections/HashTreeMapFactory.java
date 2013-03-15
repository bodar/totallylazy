package com.googlecode.totallylazy.collections;

import static com.googlecode.totallylazy.Unchecked.cast;

public class HashTreeMapFactory<K, V> extends AbstractMapFactory<K, V, HashTreeMap<K, V>> {
    private static final HashTreeMapFactory<?,?> instance = new HashTreeMapFactory<Object, Object>();
    private HashTreeMapFactory() {}

    public static <K,V> HashTreeMapFactory<K, V> factory() {return cast(instance);}

    @Override
    public HashTreeMap<K, V> empty() {
        return HashTreeMap.hashTreeMap();
    }
}
