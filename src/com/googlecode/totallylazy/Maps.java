package com.googlecode.totallylazy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Maps {
    public static <K, V> Sequence<Pair<K, V>> pairs(final Map<K, V> map) {
        return entries(map).map(Maps.<K, V>entryToPair());
    }

    public static <K, V> Sequence<Map.Entry<K, V>> entries(final Map<K, V> map) {
        return sequence(map.entrySet());
    }

    public static <K, V> Map<K, V> map(final Pair<K, V>... entries) {
        return map(sequence(entries));
    }

    public static <K, V> Map<K, V> map(final Map<K, V> seed, final Pair<K, V>... entries) {
        return map(seed, sequence(entries));
    }

    public static <K, V> Map<K, V> map(final Iterable<Pair<K, V>> entries) {
        return map(new LinkedHashMap<K, V>(), entries);
    }

    public static <K, V> Map<K, V> map(final Map<K, V> seed, final Iterable<Pair<K, V>> entries) {
        for (Pair<K, V> entry : entries) {
            seed.put(entry.first(), entry.second());
        }
        return seed;
    }

    public static <T, Key> Map<Key, T> map(final Iterator<T> iterator, final Callable1<? super T, Key> callable) {
       return map(new LinkedHashMap<Key, T>(), iterator, callable);
    }

    public static <T, Key> Map<Key, T> map(final Map<Key, T> seed, final Iterator<T> iterator, final Callable1<? super T, Key> callable) {
        while (iterator.hasNext()) {
            final T next = iterator.next();
            final Key key = Callers.call(callable, next);
            seed.put(key, next);
        }
        return seed;
    }

    public static <T, Key> Map<Key, T> map(final Iterable<T> iterable, final Callable1<? super T, Key> callable) {
        return map(iterable.iterator(), callable);
    }

    public static <T, Key> Map<Key, T> map(final Map<Key, T> seed, final Iterable<T> iterable, final Callable1<? super T, Key> callable) {
        return map(seed, iterable.iterator(), callable);
    }
    
    public static <K, V> Map<K, List<V>> multiMap(final Pair<K, V>... entries) {
        return multiMap(sequence(entries));
    }

    public static <K, V> Map<K, List<V>> multiMap(final Map<K, List<V>> seed, final Pair<K, V>... entries) {
        return multiMap(seed, sequence(entries));
    }

    public static <K, V> Map<K, List<V>> multiMap(final Iterable<Pair<K, V>> entries) {
        return multiMap(new LinkedHashMap<K, List<V>>(), entries);
    }

    public static <K, V> Map<K, List<V>> multiMap(final Map<K, List<V>> seed, final Iterable<Pair<K, V>> entries) {
        for (Pair<K, V> entry : entries) {
            if (!seed.containsKey(entry.first())) {
                seed.put(entry.first(), new ArrayList<V>());
            }
            seed.get(entry.first()).add(entry.second());
        }
        return seed;
    }
    
    public static <T, Key> Map<Key, List<T>> multiMap(final Iterator<T> iterator, final Callable1<? super T, Key> callable) {
       return multiMap(new LinkedHashMap<Key, List<T>>(), iterator, callable);
    }

    public static <T, Key> Map<Key, List<T>> multiMap(final Map<Key, List<T>> seed, final Iterator<T> iterator, final Callable1<? super T, Key> callable) {
        while (iterator.hasNext()) {
            final T next = iterator.next();
            final Key key = Callers.call(callable, next);
            if (!seed.containsKey(key)) {
                seed.put(key, new ArrayList<T>());
            }
            seed.get(key).add(next);
        }
        return seed;
    }

    public static <T, Key> Map<Key, List<T>> multiMap(final Iterable<T> iterable, final Callable1<? super T, Key> callable) {
        return multiMap(iterable.iterator(), callable);
    }

    public static <T, Key> Map<Key, List<T>> multiMap(final Map<Key, List<T>> seed, final Iterable<T> iterable, final Callable1<? super T, Key> callable) {
        return multiMap(seed, iterable.iterator(), callable);
    }

    public static <K, V> Callable2<? super Map<K, List<V>>, ? super Pair<K, V>, Map<K, List<V>>> asMultiValuedMap() {
        return new Callable2<Map<K, List<V>>, Pair<K, V>, Map<K, List<V>>>() {
            public Map<K, List<V>> call(Map<K, List<V>> map, Pair<K, V> pair) throws Exception {
                if (!map.containsKey(pair.first())) {
                    map.put(pair.first(), new ArrayList<V>());
                }
                map.get(pair.first()).add(pair.second());
                return map;
            }
        };
    }

    public static <K, V> Callable2<? super Map<K, List<V>>, ? super Pair<K, V>, Map<K, List<V>>> asMultiValuedMap(Class<K> key, Class<V> value) {
        return asMultiValuedMap();
    }

    public static <K, V> Callable2<? super Map<K, V>, ? super Pair<K, V>, Map<K, V>> asMap() {
        return new Callable2<Map<K, V>, Pair<K, V>, Map<K, V>>() {
            public Map<K, V> call(Map<K, V> map, Pair<K, V> pair) throws Exception {
                map.put(pair.first(), pair.second());
                return map;
            }
        };
    }

    public static <K, V> Callable2<? super Map<K, V>, ? super Pair<K, V>, Map<K, V>> asMap(Class<K> key, Class<V> value) {
        return asMap();
    }

    public static <K, V> Callable1<Pair<K, V>, Map.Entry<K, V>> pairToEntry() {
        return new Callable1<Pair<K, V>, Map.Entry<K, V>>() {
            public final Map.Entry<K, V> call(final Pair<K, V> pair) throws Exception {
                return new PairEntry<K, V>(pair);
            }
        };
    }

    public static <K, V> Callable1<Pair<K, V>, Map.Entry<K, V>> pairToEntry(final Class<K> keyClass, final Class<V> valueClass) {
        return pairToEntry();
    }

    public static <K, V> Callable1<Map.Entry<K, V>, Pair<K, V>> entryToPair() {
        return new Callable1<Map.Entry<K, V>, Pair<K, V>>() {
            public final Pair<K, V> call(final Map.Entry<K, V> entry) throws Exception {
                return pair(entry.getKey(), entry.getValue());
            }
        };
    }

    public static <K, V> Callable1<Map.Entry<K, V>, Pair<K, V>> entryToPair(final Class<K> keyClass, final Class<V> valueClass) {
        return entryToPair();
    }
    
    private static class PairEntry<K, V> implements Map.Entry<K, V> {
        private final Pair<K, V> pair;

        public PairEntry(Pair<K, V> pair) {
            this.pair = pair;
        }

        public K getKey() {
            return pair.first();
        }

        public V getValue() {
            return pair.second();
        }

        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof PairEntry && ((PairEntry) obj).pair.equals(pair);
        }

        @Override
        public int hashCode() {
            return pair.hashCode();
        }

        @Override
        public String toString() {
            return pair.toString();
        }
    }
}
