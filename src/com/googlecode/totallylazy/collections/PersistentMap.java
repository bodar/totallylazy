package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Foldable;
import com.googlecode.totallylazy.Functor;
import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Unchecked;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentMap;

public interface PersistentMap<K, V> extends Map<K,V>, Iterable<Pair<K, V>>, Segment<Pair<K, V>>, QueryCollection<K>, Functor<V>, Foldable<Pair<K,V>> {
    Option<V> lookup(K key);

    Option<V> find(Predicate<? super K> predicate);

    PersistentMap<K, V> insert(K key, V value);

    PersistentMap<K, V> delete(K key);

    PersistentMap<K, V> filterKeys(Predicate<? super K> predicate);

    PersistentMap<K, V> filterValues(Predicate<? super V> predicate);

    @Override
    PersistentMap<K, V> empty();

    @Override
    PersistentMap<K, V> cons(Pair<K, V> head);

    @Override
    PersistentMap<K, V> tail() throws NoSuchElementException;

    @Override
    <NewV> PersistentMap<K, NewV> map(Callable1<? super V, ? extends NewV> transformer);

    PersistentList<Pair<K, V>> toPersistentList();

    Sequence<Pair<K, V>> toSequence();

    Sequence<K> keys();

    @Override
    Sequence<V> values();

    Map<K, V> toMutableMap();

    ConcurrentMap<K, V> toConcurrentMap();

    /** @deprecated Not type safe: Replaced by {@link PersistentMap#delete(K)} */
    @Override @Deprecated
    V remove(Object o);

    /** @deprecated Not type safe: Replaced by {@link PersistentMap#insert(K, V)} */
    @Override @Deprecated
    V put(K key, V value);

    /** @deprecated Not type safe: Replaced by {@link PersistentMap#insert(K, V)} */
    @Override @Deprecated
    void putAll(Map<? extends K, ? extends V> m);

    /** @deprecated Not type safe: Replaced by {@link PersistentMap#empty()} */
    @Override @Deprecated
    void clear();

    class functions {
        public static <K,V> Mapper<PersistentMap<K, V>, Option<V>> get(final K key) {
            return new Mapper<PersistentMap<K, V>, Option<V>>() {
                @Override
                public Option<V> call(PersistentMap<K, V> map) throws Exception {
                    return map.lookup(key);
                }
            };
        }

        public static <K, V> Mapper<PersistentMap<K, V>, PersistentMap<K, V>> remove(final K key) {
            return new Mapper<PersistentMap<K, V>, PersistentMap<K, V>>() {
                @Override
                public PersistentMap<K, V> call(PersistentMap<K, V> map) throws Exception {
                    return map.delete(key);
                }
            };
        }

        public static <K,V> Mapper<PersistentMap<K, V>, Boolean> contains(final Object other) {
            return new Mapper<PersistentMap<K, V>, Boolean>() {
                @Override
                public Boolean call(PersistentMap<K, V> map) throws Exception {
                    return map.contains(other);
                }
            };
        }
    }

    class methods {
        public static <K,V> Map<K,V> toMap(PersistentMap<K,V> source) {
            return Maps.map(source);
        }

        public static <K, V, M extends PersistentMap<K,V>> Pair<M,Option<V>> put(M map, K key, V newValue) {
            return Pair.pair(Unchecked.<M>cast(map.insert(key, newValue)),map.lookup(key));
        }

        public static <K, V, M extends PersistentMap<K,V>> Pair<M,Option<V>> remove(M map, K key) {
            return Pair.pair(Unchecked.<M>cast(map.delete(key)),map.lookup(key));
        }
    }
}
