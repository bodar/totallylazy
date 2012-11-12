package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicates;
import org.junit.Test;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.contains;
import static com.googlecode.totallylazy.collections.ListMap.emptyListMap;
import static com.googlecode.totallylazy.collections.ListMap.listMap;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListMapTest extends MapContract {
    @Override
    protected <K, V> ImmutableMap<K, V> empty(Class<K> kClass, Class<V> vClass) {
        return emptyListMap();
    }

    @Override
    protected <K, V> ImmutableMap<K, V> map(K key, V value) {
        return listMap(key, value);
    }

    @Override
    protected <K, V> ImmutableMap<K, V> map(K key1, V value1, K key2, V value2) {
        return listMap(key1, value1, key2, value2);
    }

    @Override
    protected <K, V> ImmutableMap<K, V> map(K key1, V value1, K key2, V value2, K key3, V value3) {
        return listMap(key1, value1, key2, value2, key3, value3);
    }

    @Override
    protected <K, V> ImmutableMap<K, V> map(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
        return listMap(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    @Override
    protected <K, V> ImmutableMap<K, V> map(Iterable<? extends Pair<K, V>> iterable) {
        return listMap(iterable);
    }

    @Test
    public void preservesOrder(){
        assertThat(listMap(4, "Alex", 1, "Dan", 3, "Stu", 2, "Ray"), hasExactly(pair(4, "Alex"), pair(1, "Dan"), pair(3, "Stu"), pair(2, "Ray")));
    }

}
