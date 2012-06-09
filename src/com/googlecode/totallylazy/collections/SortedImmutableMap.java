package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;

public interface SortedImmutableMap<K, V> extends ImmutableMap<K, V> {
    @Override
    SortedImmutableMap<K, V> cons(Pair<K, V> head);

    @Override
    SortedImmutableMap<K, V> put(K key, V value);

    @Override
    SortedImmutableMap<K, V> remove(K key);

    @Override
    SortedImmutableMap<K, V> filterKeys(Predicate<? super K> predicate);

    @Override
    SortedImmutableMap<K, V> filterValues(Predicate<? super V> predicate);

    @Override
    <NewV> SortedImmutableMap<K, NewV> mapValues(Callable1<? super V, ? extends NewV> transformer);

    Pair<SortedImmutableMap<K, V>, Pair<K, V>> removeMinimum();

    Pair<SortedImmutableMap<K, V>, Pair<K, V>> removeMaximum();

    V index(int i);
}
