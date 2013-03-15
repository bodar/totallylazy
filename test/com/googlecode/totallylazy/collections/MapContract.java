package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Lists;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Sequence;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.contains;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.fail;

public abstract class MapContract {
    protected abstract <K extends Comparable<K>, V> MapFactory<K, V, ? extends PersistentMap<K, V>> factory();

    protected <K extends Comparable<K>, V> PersistentMap<K, V> empty(Class<K> kClass, Class<V> vClass) {
        return this.<K, V>factory().empty();
    }

    protected <K extends Comparable<K>, V> PersistentMap<K, V> empty() {
        return this.<K, V>factory().empty();
    }

    protected <K extends Comparable<K>, V> PersistentMap<K, V> map() {
        return empty();
    }

    protected <K extends Comparable<K>, V> PersistentMap<K, V> map(K key, V value) {
        return this.<K, V>factory().map(key, value);
    }

    protected <K extends Comparable<K>, V> PersistentMap<K, V> map(K key1, V value1, K key2, V value2) {
        return this.<K, V>factory().map(key1, value1, key2, value2);
    }

    protected <K extends Comparable<K>, V> PersistentMap<K, V> map(K key1, V value1, K key2, V value2, K key3, V value3) {
        return this.<K, V>factory().map(key1, value1, key2, value2, key3, value3);
    }

    protected <K extends Comparable<K>, V> PersistentMap<K, V> map(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
        return this.<K, V>factory().map(key1, value1, key2, value2, key3, value3, key4, value4);
    }

    protected <K extends Comparable<K>, V> PersistentMap<K, V> map(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
        return this.<K, V>factory().map(key1, value1, key2, value2, key3, value3, key4, value4, key5, value5);
    }

    protected <K extends Comparable<K>, V> PersistentMap<K, V> map(Iterable<? extends Pair<K, V>> iterable) {
        return this.<K, V>factory().map(iterable);
    }

    @Test
    public void canGet() throws Exception {
        PersistentMap<Integer, String> map = map(1, "Dan", 2, "Ray", 3, "Stu");
        assertThat(map.get(2), is(some("Ray")));
        assertThat(map.get(4), is(none(String.class)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void canPut() throws Exception {
        PersistentMap<Integer, String> map = map(1, "Dan").put(3, "Stu").put(2, "Ray");
        assertThat(map, containsInAnyOrder(pair(1, "Dan"), pair(3, "Stu"), pair(2, "Ray")));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void canRemove() throws Exception {
        final PersistentMap<Integer, String> map = map(4, "Alex", 1, "Dan", 3, "Stu", 2, "Ray");
        assertThat(map.remove(4), containsInAnyOrder(pair(1, "Dan"), pair(3, "Stu"), pair(2, "Ray")));
        assertThat(map.remove(3), containsInAnyOrder(pair(4, "Alex"), pair(1, "Dan"), pair(2, "Ray")));
        assertThat(map.remove(2), containsInAnyOrder(pair(4, "Alex"), pair(1, "Dan"), pair(3, "Stu")));
        assertThat(map.remove(1), containsInAnyOrder(pair(4, "Alex"), pair(3, "Stu"), pair(2, "Ray")));
        assertThat(map.remove(0), is(map));
    }

    @Test
    public void putReplacesValuesWithSameKey() throws Exception {
        PersistentMap<Integer, String> map = map(1, "Dan").put(3, "Stu").put(1, "Ray");
        assertThat(map, hasExactly(pair(1, "Ray"), pair(3, "Stu")));
    }

    @Test
    public void canCheckContains() throws Exception {
        PersistentMap<Integer, String> map = map(1, "Dan", 2, "Ray", 3, "Stu");
        assertThat(map.contains(2), is(true));
        assertThat(map.contains(4), is(false));
    }

    @Test
    public void canCreateFromAnIterable() throws Exception {
        PersistentMap<Integer, String> map = map(sequence(pair(1, "Dan"), pair(2, "Ray"), pair(3, "Stu")));
        assertThat(map.contains(2), is(true));
        assertThat(map.contains(4), is(false));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void canConvertToPersistentList() throws Exception {
        PersistentList<Pair<Integer, String>> map = map(2, "Ray", 1, "Dan", 3, "Stu").toPersistentList();
        assertThat(map, containsInAnyOrder(pair(2, "Ray"), pair(1, "Dan"), pair(3, "Stu")));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void canJoin() throws Exception {
        PersistentMap<Integer, String> map = map(3, "Stu", 4, "Matt").joinTo(map(1, "Dan", 2, "Ray"));
        assertThat(map, containsInAnyOrder(pair(1, "Dan"), pair(2, "Ray"), pair(3, "Stu"), pair(4, "Matt")));
    }

    @Test
    public void supportsFindingAValueAsAnOption() throws Exception {
        assertThat(map("Dan", 2).find(contains("a")), is(some(2)));
        assertThat(map("Dan", 2).find(contains("b")), is(none(Integer.class)));
    }

    @Test
    public void supportsFilteringByKey() throws Exception {
        assertThat(map("Dan", 2).filterKeys(contains("a")), is(map("Dan", 2)));
        assertThat(map("Dan", 2).filterKeys(contains("b")), is(empty(String.class, Integer.class)));
    }

    @Test
    public void supportsFilteringByValue() throws Exception {
        assertThat(map("Dan", 2).filterValues(Predicates.is(2)), is(map("Dan", 2)));
        assertThat(map("Dan", 2).filterValues(Predicates.is(3)), is(empty(String.class, Integer.class)));
    }

    @Test
    public void supportsMappingValues() throws Exception {
        assertThat(map("Dan", 2).map(add(2)), is(map("Dan", (Number) 4)));
    }

    @Test
    public void canIterate() throws Exception {
        final Iterator<Pair<Integer, Integer>> iterator = map(0, 0, 1, 1, 2, 2).iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next().first(), is(0));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next().first(), is(1));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next().first(), is(2));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void canGetKeys() throws Exception {
        Sequence<Integer> keys = map(4, "Alex", 1, "Dan", 3, "Stu", 2, "Ray").keys();
        assertThat(keys, containsInAnyOrder(1,2,3,4));
    }

    @Test
    public void supportsIsEmpty() throws Exception {
        assertThat(empty(Integer.class, String.class).isEmpty(), is(true));
        assertThat(map(1,"2").isEmpty(), is(false));
        assertThat(map(1,"2").remove(1).isEmpty(), is(true));
    }

    @Test
    public void supportsHead() throws Exception {
        assertThat(map(1,"2").head(), is(pair(1, "2")));
        try {
            empty(Integer.class, String.class).head();
            fail("NoSuchElementException expect on calling head on empty map");
        } catch (NoSuchElementException ignore) {
        }
    }

    @Test
    public void supportsHeadOption() throws Exception {
        assertThat(map(1,"2").headOption(), is(some(pair(1, "2"))));
        assertThat(empty(Integer.class, String.class).headOption(), is(Option.<Pair<Integer, String >>none()));
    }

    @Test
    public void supportsTail() throws Exception {
        assertThat(map(1,"2").tail().isEmpty(), is(true));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void supportsFold() throws Exception {
        assertThat(map(1, "2").fold(Lists.list(pair(0, "1")), Lists.functions.<Pair<Integer, String>>add()), is(Lists.list(pair(0, "1"), pair(1, "2"))));
    }

}
