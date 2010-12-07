package com.googlecode.totallylazy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Maps {
    public static <K,V> HashMap<K, V> map(Pair<K, V>... entries) {
        HashMap<K, V> map = new HashMap<K, V>();
        for (Pair<K, V> entry : entries) {
            map.put(entry.first(), entry.second());
        }
        return map;
    }

    public static <K,V> HashMap<K, V> map(Class<K> key, Class<V> value, Pair<K, V>... entries) {
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
}
