package com.googlecode.totallylazy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Pair.pair;

public class Maps {
    public static <K,V> HashMap<K, V> map(Pair<? extends K, ? extends V>... entries) {
        HashMap<K, V> map = new HashMap<K, V>();
        for (Pair<? extends K, ? extends V> entry : entries) {
            map.put(entry.first(), entry.second());
        }
        return map;
    }

    public static <K,V> HashMap<K, V> map(Class<K> key, Class<V> value, Pair<? extends K, ? extends V>... entries) {
        return map(entries);
    }

    public static <K,V> Callable2<? super Map<K, List<V>>,? super Pair<K, V>, Map<K, List<V>>> asMultiValuedMap() {
        return new Callable2<Map<K, List<V>>, Pair<K, V>, Map<K, List<V>>>() {
            public Map<K, List<V>> call(Map<K, List<V>> map, Pair<K, V> pair) throws Exception {
                if(!map.containsKey(pair.first())){
                    map.put(pair.first(), new ArrayList<V>());
                }
                map.get(pair.first()).add(pair.second());
                return map;
            }
        };
    }

    public static <K,V> Callable2<? super Map<K, List<V>>,? super Pair<K, V>, Map<K, List<V>>> asMultiValuedMap(Class<K> key, Class<V> value) {
        return asMultiValuedMap();
    }

    public static <K,V> Callable2<? super Map<K, V>,? super Pair<K, V>, Map<K, V>> asMap() {
        return new Callable2<Map<K, V>, Pair<K, V>, Map<K, V>>() {
            public Map<K, V> call(Map<K, V> map, Pair<K, V> pair) throws Exception {
                map.put(pair.first(), pair.second());
                return map;
            }
        };
    }

    public static <K,V> Callable2<? super Map<K, V>,? super Pair<K, V>, Map<K, V>> asMap(Class<K> key, Class<V> value) {
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
