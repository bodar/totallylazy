package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.ImmutableCollection;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Unchecked;

import java.util.Map;
import java.util.NoSuchElementException;

public interface ImmutableMap<K, V> extends Iterable<Pair<K, V>>, Segment<Pair<K, V>>, ImmutableCollection<K>, Functor<V> {
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

    Map<K, V> toMap();

    class functions {
        public static <K, V> Function1<ImmutableMap<K, V>, ImmutableMap<K, V>> remove(final K key) {
            return new Function1<ImmutableMap<K, V>, ImmutableMap<K, V>>() {
                @Override
                public ImmutableMap<K, V> call(ImmutableMap<K, V> map) throws Exception {
                    return map.remove(key);
                }
            };
        }
    }

    class methods {
        public static <K,V> Map<K,V> toMap(ImmutableMap<K,V> source) {
            return Maps.map(source);
        }

        public static <K, V, M extends ImmutableMap<K,V>> Pair<M,Option<V>> put(M map, K key, V newValue) {
            return Pair.pair(Unchecked.<M>cast(map.put(key, newValue)),map.get(key));
        }

        public static <K, V, M extends ImmutableMap<K,V>> Pair<M,Option<V>> remove(M map, K key) {
            return Pair.pair(Unchecked.<M>cast(map.remove(key)),map.get(key));
        }
    }
}
