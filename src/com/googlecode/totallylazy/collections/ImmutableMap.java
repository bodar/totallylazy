package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Container;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.comparators.Comparators;

import java.util.Comparator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;

public interface ImmutableMap<K, V> extends Iterable<Pair<K, V>>, Segment<Pair<K, V>>, Container<K>, Functor<V> {
    @Override
    ImmutableMap<K, V> cons(Pair<K, V> head);

    @Override
    ImmutableMap<K, V> tail() throws NoSuchElementException;

    Option<V> get(K key);

    Option<V> find(Predicate<? super K> predicate);

    ImmutableMap<K, V> put(K key, V value);

    ImmutableMap<K, V> remove(K key);

    ImmutableMap<K, V> filterKeys(Predicate<? super K> predicate);

    ImmutableMap<K, V> filterValues(Predicate<? super V> predicate);

    @Override
    <NewV> ImmutableMap<K, NewV> map(Callable1<? super V, ? extends NewV> transformer);

    int size();

    ImmutableList<Pair<K, V>> immutableList();

    class functions{
        public static <K, V> Function1<ImmutableMap<K, V>, ImmutableMap<K, V>> remove(final K key) {
            return new Function1<ImmutableMap<K, V>, ImmutableMap<K, V>>() {
                @Override
                public ImmutableMap<K, V> call(ImmutableMap<K, V> map) throws Exception {
                    return map.remove(key);
                }
            };
        }
    }
}
