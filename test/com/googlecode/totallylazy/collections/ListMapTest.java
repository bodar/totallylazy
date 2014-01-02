package com.googlecode.totallylazy.collections;

import org.junit.Test;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.collections.ListMap.listMap;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListMapTest extends MapContract {

    @Override
    protected <K extends Comparable<K>, V> MapFactory<K, V, ? extends PersistentMap<K, V>> factory() {
        return ListMap.<K, V>factory();
    }

    @Test
    public void preservesOrder() {
        assertThat(listMap(4, "Alex", 1, "Dan", 3, "Stu", 2, "Ray"), hasExactly(pair(4, "Alex"), pair(1, "Dan"), pair(3, "Stu"), pair(2, "Ray")));
    }

}
