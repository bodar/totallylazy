package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicates;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static com.googlecode.totallylazy.Maps.filterValues;
import static com.googlecode.totallylazy.Maps.mapKeys;
import static com.googlecode.totallylazy.Maps.mapValues;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.contains;
import static com.googlecode.totallylazy.Strings.toLowerCase;
import static com.googlecode.totallylazy.collections.ImmutableMap.constructors.map;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static org.hamcrest.MatcherAssert.assertThat;

public class TreeMapTest {
    @Test
    public void canPut() throws Exception {
        ImmutableMap<Integer, String> map = map(1, "Dan").put(3, "Stu").put(2, "Ray");
        assertThat(map, hasExactly(pair(1, "Dan"), pair(2, "Ray"), pair(3, "Stu")));
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
        ImmutableList<Pair<Integer, String>> map = map(1, "Dan", 2, "Ray", 3, "Stu").immutableList();
        assertThat(map, hasExactly(pair(1, "Dan"), pair(2, "Ray"), pair(3, "Stu")));
    }

    @Test
    public void canJoin() throws Exception {
        ImmutableMap<Integer, String> map = map(1, "Dan", 2, "Ray").joinTo(map(4, "Matt", 3, "Stu"));
        assertThat(map, hasExactly(pair(1, "Dan"), pair(2, "Ray"), pair(3, "Stu"), pair(4, "Matt")));
    }

    @Test
    public void canGet() throws Exception {
        ImmutableMap<Integer, String> map = map(1, "Dan", 2, "Ray", 3, "Stu");
        assertThat(map.get(2), is(some("Ray")));
        assertThat(map.get(4), is(none(String.class)));
    }

    @Test
    public void supportsFindingAValueAsAnOption() throws Exception {
        assertThat(map("Dan", 2).find(contains("a")), is(some(2)));
        assertThat(map("Dan", 2).find(contains("b")), is(none(Integer.class)));
    }

    @Test
    public void supportsFilteringByKey() throws Exception {
        assertThat(map("Dan", 2).filterKeys(contains("a")), is(map("Dan", 2)));
        assertThat(map("Dan", 2).filterKeys(contains("b")), is(ImmutableMap.constructors.<String, Integer>empty()));
    }

    @Test
    public void supportsFilteringByValue() throws Exception {
        assertThat(map("Dan", 2).filterValues(Predicates.is(2)), is(ImmutableMap.constructors.map("Dan", 2)));
        assertThat(map("Dan", 2).filterValues(Predicates.is(3)), is(ImmutableMap.constructors.<String, Integer>empty()));
    }

    @Test
    public void supportsMappingValues() throws Exception {
        assertThat(map("Dan", 2).mapValues(add(2)), is(map("Dan", (Number) 4)));
    }
}
