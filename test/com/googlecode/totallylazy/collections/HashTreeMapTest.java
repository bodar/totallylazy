package com.googlecode.totallylazy.collections;

public class HashTreeMapTest extends MapContract {

    @Override
    protected <K extends Comparable<K>, V> MapFactory<K, V, ? extends PersistentMap<K, V>> factory() {
        return HashTreeMap.<K, V>factory();
    }
}
