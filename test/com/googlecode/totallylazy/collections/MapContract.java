package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicates;
import org.junit.Test;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.contains;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static org.hamcrest.MatcherAssert.assertThat;

public abstract class MapContract {
    protected abstract <K,V> ImmutableMap<K, V> empty(Class<K> kClass, Class<V> vClass);
    protected abstract <K,V> ImmutableMap<K, V> map(K key, V value);
    protected abstract <K,V> ImmutableMap<K, V> map(K key1, V value1, K key2, V value2);
    protected abstract <K,V> ImmutableMap<K, V> map(K key1, V value1, K key2, V value2, K key3, V value3);
    protected abstract <K,V> ImmutableMap<K, V> map(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4);
    protected abstract <K,V> ImmutableMap<K, V> map(Iterable<? extends Pair<K, V>> iterable);

    @Test
    public void canGet() throws Exception {
        ImmutableMap<Integer, String> map = map(1, "Dan", 2, "Ray", 3, "Stu");
        assertThat(map.get(2), is(some("Ray")));
        assertThat(map.get(4), is(none(String.class)));
    }

    @Test
    public void canPut() throws Exception {
        ImmutableMap<Integer, String> map = map(1, "Dan").put(3, "Stu").put(2, "Ray");
        assertThat(map, hasExactly(pair(1, "Dan"), pair(3, "Stu"), pair(2, "Ray")));
    }

    @Test
    public void canRemove() throws Exception {
        final ImmutableMap<Integer, String> map = map(4, "Alex", 1, "Dan", 3, "Stu", 2, "Ray");
        assertThat(map.remove(4), hasExactly(pair(1, "Dan"), pair(3, "Stu"), pair(2, "Ray")));
        assertThat(map.remove(3), hasExactly(pair(4, "Alex"), pair(1, "Dan"), pair(2, "Ray")));
        assertThat(map.remove(2), hasExactly(pair(4, "Alex"), pair(1, "Dan"), pair(3, "Stu")));
        assertThat(map.remove(1), hasExactly(pair(4, "Alex"), pair(3, "Stu"), pair(2, "Ray")));
        assertThat(map.remove(0), is(map));
    }

    @Test
    public void putReplacesValuesWithSameKey() throws Exception {
        ImmutableMap<Integer, String> map = map(1, "Dan").put(3, "Stu").put(1, "Ray");
        assertThat(map, hasExactly(pair(1, "Ray"), pair(3, "Stu")));
    }

    @Test
    public void canCheckContains() throws Exception {
        ImmutableMap<Integer, String> map = map(1, "Dan", 2, "Ray", 3, "Stu");
        assertThat(map.contains(2), is(true));
        assertThat(map.contains(4), is(false));
    }

    @Test
    public void canCreateATreeFromAnIterable() throws Exception {
        ImmutableMap<Integer, String> map = map(sequence(pair(1, "Dan"), pair(2, "Ray"), pair(3, "Stu")));
        assertThat(map.contains(2), is(true));
        assertThat(map.contains(4), is(false));
    }

    @Test
    public void canConvertToPersistentList() throws Exception {
        ImmutableList<Pair<Integer, String>> map = map(2, "Ray", 1, "Dan", 3, "Stu").immutableList();
        assertThat(map, hasExactly(pair(2, "Ray"), pair(1, "Dan"), pair(3, "Stu")));
    }

    @Test
    public void canJoin() throws Exception {
        ImmutableMap<Integer, String> map = map(3, "Stu", 4, "Matt").joinTo(map(1, "Dan", 2, "Ray"));
        assertThat(map, is(map(1, "Dan", 2, "Ray", 3, "Stu", 4, "Matt")));
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
}
