package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.callables.TimeReport;
import org.junit.Test;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.contains;
import static com.googlecode.totallylazy.collections.ImmutableSortedMap.constructors.emptySortedMap;
import static com.googlecode.totallylazy.collections.ImmutableSortedMap.constructors.sortedMap;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static com.googlecode.totallylazy.numbers.Numbers.range;
import static org.hamcrest.MatcherAssert.assertThat;

public class ImmutableSortedMapTest {
    @Test
    public void creatingASortedMapFromAnIterableIsFast() throws Exception {
        //in order - fold / cons Elapsed msecs for 11 runs:	Avg:13.460369555555555	Min:8.874518	Max:99.661891	Total:229.67973499999997
        //in order - sorted list Elapsed msecs for 11 runs:	Avg:11.016874222222222	Min:6.171428	Max:101.041891	Total:206.36518700000005
        // shuffle - fold / cons Elapsed msecs for 11 runs:	Avg:19.289151888888888	Min:13.316564	Max:113.912953	Total:300.831884
        // shuffle - sorted list Elapsed msecs for 11 runs:	Avg:15.702062000000002	Min:8.892117	Max:125.818735	Total:276.02941000000004
        final Sequence<Integer> integers = range(0, 10000).safeCast(Integer.class).shuffle();
        TimeReport time = TimeReport.time(10, new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return sortedMap(integers.map(asPair()));
            }
        });
        System.out.println(time);
    }

    public static Callable1<Integer, Pair<Integer, String>> asPair() {
        return new Callable1<Integer, Pair<Integer, String>>() {
            @Override
            public Pair<Integer, String> call(Integer integer) throws Exception {
                return Pair.pair(integer, integer.toString());
            }
        };
    }


    @Test
    public void canGetFirst() throws Exception {
        assertThat(sortedMap(4, "Alex", 1, "Dan", 3, "Stu", 2, "Ray").first(), is(pair(1, "Dan")));
    }

    @Test
    public void canRemoveFirst() throws Exception {
        final Pair<? extends ImmutableSortedMap<Integer, String>, Pair<Integer, String>> result = sortedMap(4, "Alex", 1, "Dan", 3, "Stu", 2, "Ray").removeFirst();
        assertThat(result.first(), hasExactly(pair(2, "Ray"), pair(3, "Stu"), pair(4, "Alex")));
        assertThat(result.second(), is(pair(1, "Dan")));
    }

    @Test
    public void canGetLast() throws Exception {
        assertThat(sortedMap(4, "Alex", 1, "Dan", 3, "Stu", 2, "Ray").last(), is(pair(4, "Alex")));
    }

    @Test
    public void canRemoveLast() throws Exception {
        final Pair<? extends ImmutableSortedMap<Integer, String>, Pair<Integer, String>> result = sortedMap(4, "Alex", 1, "Dan", 3, "Stu", 2, "Ray").removeLast();
        assertThat(result.first(), hasExactly(pair(1, "Dan"), pair(2, "Ray"), pair(3, "Stu")));
        assertThat(result.second(), is(pair(4, "Alex")));
    }

    @Test
    public void canRemove() throws Exception {
        final ImmutableMap<Integer, String> map = sortedMap(4, "Alex", 1, "Dan", 3, "Stu", 2, "Ray");
        assertThat(map.remove(4), hasExactly(pair(1, "Dan"), pair(2, "Ray"), pair(3, "Stu")));
        assertThat(map.remove(3), hasExactly(pair(1, "Dan"), pair(2, "Ray"), pair(4, "Alex")));
        assertThat(map.remove(2), hasExactly(pair(1, "Dan"), pair(3, "Stu"), pair(4, "Alex")));
        assertThat(map.remove(1), hasExactly(pair(2, "Ray"), pair(3, "Stu"), pair(4, "Alex")));
        assertThat(map.remove(0), is(map));
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
        assertThat(sortedMap("Dan", 2).filterKeys(contains("b")), is(emptySortedMap(String.class, Integer.class)));
    }

    @Test
    public void supportsFilteringByValue() throws Exception {
        assertThat(sortedMap("Dan", 2).filterValues(Predicates.is(2)), is(sortedMap("Dan", 2)));
        assertThat(sortedMap("Dan", 2).filterValues(Predicates.is(3)), is(emptySortedMap(String.class, Integer.class)));
    }

    @Test
    public void supportsMappingValues() throws Exception {
        assertThat(sortedMap("Dan", 2).mapValues(add(2)), is(sortedMap("Dan", (Number) 4)));
    }
}
