package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicates;
import org.junit.Test;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.contains;
import static com.googlecode.totallylazy.collections.ImmutableMap.constructors.sortedMap;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static org.hamcrest.MatcherAssert.assertThat;

public class SortedMapTest {
    @Test
    public void canRemove() throws Exception {
        assertThat(sortedMap(4, "Alex", 1, "Dan", 3, "Stu", 2, "Ray").remove(4), hasExactly(pair(1, "Dan"), pair(2, "Ray"), pair(3, "Stu")));
    }

    @Test
    public void canRemoveIsNotBuggy() throws Exception {
        assertThat(sortedMap(4, "Alex", 1, "Dan", 3, "Stu", 2, "Ray").remove(2), hasExactly(pair(1, "Dan"), pair(3, "Stu"), pair(4, "Alex")));
    }

    @Test
    public void canPut() throws Exception {
        ImmutableMap<Integer, String> map = sortedMap(1, "Dan").put(3, "Stu").put(2, "Ray");
        assertThat(map, hasExactly(pair(1, "Dan"), pair(2, "Ray"), pair(3, "Stu")));
    }

    @Test
    public void canCheckContains() throws Exception {
        ImmutableMap<Integer, String> map = sortedMap(1, "Dan", 2, "Ray", 3, "Stu");
        assertThat(map.contains(2), is(true));
        assertThat(map.contains(4), is(false));
    }

    @Test
    public void canCreateATreeFromAnIterable() throws Exception {
        ImmutableMap<Integer, String> map = sortedMap(sequence(pair(1, "Dan"), pair(2, "Ray"), pair(3, "Stu")));
        assertThat(map.contains(2), is(true));
        assertThat(map.contains(4), is(false));
    }

    @Test
    public void canConvertToPersistentList() throws Exception {
        ImmutableList<Pair<Integer, String>> map = sortedMap(2, "Ray", 1, "Dan", 3, "Stu").immutableList();
        assertThat(map, hasExactly(pair(1, "Dan"), pair(2, "Ray"), pair(3, "Stu")));
    }

    @Test
    public void canJoin() throws Exception {
        ImmutableMap<Integer, String> map = sortedMap(1, "Dan", 2, "Ray").joinTo(sortedMap(4, "Matt", 3, "Stu"));
        assertThat(map, hasExactly(pair(1, "Dan"), pair(2, "Ray"), pair(3, "Stu"), pair(4, "Matt")));
    }

    @Test
    public void canGet() throws Exception {
        ImmutableMap<Integer, String> map = sortedMap(1, "Dan", 2, "Ray", 3, "Stu");
        assertThat(map.get(2), is(some("Ray")));
        assertThat(map.get(4), is(none(String.class)));
    }

    @Test
    public void supportsFindingAValueAsAnOption() throws Exception {
        assertThat(sortedMap("Dan", 2).find(contains("a")), is(some(2)));
        assertThat(sortedMap("Dan", 2).find(contains("b")), is(none(Integer.class)));
    }

    @Test
    public void supportsFilteringByKey() throws Exception {
        assertThat(sortedMap("Dan", 2).filterKeys(contains("a")), is(sortedMap("Dan", 2)));
        assertThat(sortedMap("Dan", 2).filterKeys(contains("b")), is(ImmutableMap.constructors.<String, Integer>sortedMap()));
    }

    @Test
    public void supportsFilteringByValue() throws Exception {
        assertThat(sortedMap("Dan", 2).filterValues(Predicates.is(2)), is(sortedMap("Dan", 2)));
        assertThat(sortedMap("Dan", 2).filterValues(Predicates.is(3)), is(ImmutableMap.constructors.<String, Integer>sortedMap()));
    }

    @Test
    public void supportsMappingValues() throws Exception {
        assertThat(sortedMap("Dan", 2).mapValues(add(2)), is(sortedMap("Dan", (Number) 4)));
    }
}
