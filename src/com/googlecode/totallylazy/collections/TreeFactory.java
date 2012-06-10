package com.googlecode.totallylazy.collections;

import java.util.Comparator;

public interface TreeFactory {
    <K, V> TreeMap<K, V> create(Comparator<K> comparator);

    <K, V> TreeMap<K, V> create(Comparator<K> comparator, K key, V value);

    <K, V> TreeMap<K, V> create(Comparator<K> comparator, TreeMap<K, V> left, K key, V value, TreeMap<K, V> right);
}
