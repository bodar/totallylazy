package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Container;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.comparators.Comparators;

import java.util.Comparator;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;

public interface ImmutableMap<K, V> extends Iterable<Pair<K, V>>, Segment<Pair<K, V>>, Container<K> {
    Option<V> get(K key);

    Option<V> find(Predicate<? super K> predicate);

    ImmutableMap<K, V> put(K key, V value);

    ImmutableMap<K, V> remove(K key);

    ImmutableMap<K, V> filterKeys(Predicate<? super K> predicate);

    ImmutableMap<K, V> filterValues(Predicate<? super V> predicate);

    <NewV> ImmutableMap<K, NewV> mapValues(Callable1<? super V, ? extends NewV> transformer);

    int size();

    ImmutableList<Pair<K, V>> immutableList();
}
