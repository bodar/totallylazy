package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Foldable;
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

public interface PersistentMap<K, V> extends Iterable<Pair<K, V>>, Segment<Pair<K, V>>, PersistentCollection<K>, Functor<V>, Foldable<Pair<K,V>> {
    @Override
    PersistentMap<K, V> cons(Pair<K, V> head);

    @Override
    PersistentMap<K, V> tail() throws NoSuchElementException;

    Option<V> get(K key);

    Option<V> find(Predicate<? super K> predicate);

    PersistentMap<K, V> put(K key, V value);

    PersistentMap<K, V> remove(K key);

    PersistentMap<K, V> filterKeys(Predicate<? super K> predicate);

    PersistentMap<K, V> filterValues(Predicate<? super V> predicate);

    @Override
    <NewV> PersistentMap<K, NewV> map(Callable1<? super V, ? extends NewV> transformer);

    PersistentList<Pair<K, V>> persistentList();

    Map<K, V> toMap();

    class functions {
        public static <K, V> Function1<PersistentMap<K, V>, PersistentMap<K, V>> remove(final K key) {
            return new Function1<PersistentMap<K, V>, PersistentMap<K, V>>() {
                @Override
                public PersistentMap<K, V> call(PersistentMap<K, V> map) throws Exception {
                    return map.remove(key);
                }
            };
        }
    }

    class methods {
        public static <K,V> Map<K,V> toMap(PersistentMap<K,V> source) {
            return Maps.map(source);
        }

        public static <K, V, M extends PersistentMap<K,V>> Pair<M,Option<V>> put(M map, K key, V newValue) {
            return Pair.pair(Unchecked.<M>cast(map.put(key, newValue)),map.get(key));
        }

        public static <K, V, M extends PersistentMap<K,V>> Pair<M,Option<V>> remove(M map, K key) {
            return Pair.pair(Unchecked.<M>cast(map.remove(key)),map.get(key));
        }
    }
}
