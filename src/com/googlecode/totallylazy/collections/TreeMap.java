package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;

import java.util.Comparator;
import java.util.NoSuchElementException;

public interface TreeMap<K, V> extends ImmutableSortedMap<K, V> {
    K key();

    V value();

    TreeMap<K,V> left();

    TreeMap<K,V> left(TreeMap<K,V> newLeft);

    TreeMap<K,V> right();

    TreeMap<K,V> right(TreeMap<K,V> newRight);

    @Override
    TreeMap<K, V> cons(Pair<K, V> head);

    @Override
    TreeMap<K, V> tail() throws NoSuchElementException;

    @Override
    TreeMap<K, V> put(K key, V value);

    @Override
    TreeMap<K, V> remove(K key);

    @Override
    TreeMap<K, V> filterKeys(Predicate<? super K> predicate);

    @Override
    TreeMap<K, V> filterValues(Predicate<? super V> predicate);

    @Override
    <NewV> TreeMap<K, NewV> mapValues(Callable1<? super V, ? extends NewV> transformer);

    @Override
    Pair<? extends TreeMap<K, V>, Pair<K, V>> removeFirst();

    @Override
    Pair<? extends TreeMap<K, V>, Pair<K, V>> removeLast();

    enum constructors implements TreeFactory {
        factory;

        @Override
        public <K, V> TreeMap<K, V> create(Comparator<K> comparator) {
            return new AbstractEmptyTreeMap<K, V, TreeMap<K, V>>(comparator, this) {
            };
        }

        @Override
        public <K, V> TreeMap<K, V> create(Comparator<K> comparator, K key, V value) {
            return create(comparator, this.<K, V>create(comparator), key, value, this.<K, V>create(comparator));
        }

        @Override
        public <K, V> TreeMap<K, V> create(Comparator<K> comparator, TreeMap<K, V> left, K key, V value, TreeMap<K, V> right) {
            return new AbstractTreeMap<K, V, TreeMap<K, V>>(left, key, value, right, comparator, constructors.factory) {
            };
        }
    }
}