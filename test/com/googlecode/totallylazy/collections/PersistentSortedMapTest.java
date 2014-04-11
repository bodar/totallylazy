package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Seq;
import com.googlecode.totallylazy.callables.TimeReport;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.contains;
import static com.googlecode.totallylazy.collections.PersistentSortedMap.constructors.emptySortedMap;
import static com.googlecode.totallylazy.collections.PersistentSortedMap.constructors.sortedMap;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static com.googlecode.totallylazy.numbers.Numbers.range;
import static org.hamcrest.MatcherAssert.assertThat;

public class PersistentSortedMapTest {
    @Test
    public void canPutAndReturnOldValue() throws Exception {
        PersistentSortedMap<Integer, String> pairs = sortedMap(4, "Four", 5, "Five", 3, "Three", 2, "Two", 6, "Six");
        Pair<PersistentSortedMap<Integer, String>, Option<String>> result = PersistentMap.methods.put(pairs, 4, "NewFour");
        assertThat(result.first().lookup(4).get(), is("NewFour"));
        assertThat(result.second().get(), is("Four"));
    }

    @Test
    public void canRemoveAndReturnOldValue() throws Exception {
        PersistentSortedMap<Integer, String> pairs = sortedMap(4, "Four", 5, "Five", 3, "Three", 2, "Two", 6, "Six");
        Pair<PersistentSortedMap<Integer, String>, Option<String>> result = PersistentMap.methods.remove(pairs, 4);
        assertThat(result.first().lookup(4).isEmpty(), is(true));
        assertThat(result.second().get(), is("Four"));
    }

    @Test
    public void supportsIndexOf() throws Exception {
        PersistentSortedMap<Integer, Integer> map = sortedMap(4, 4, 5, 5, 3, 3, 2, 2, 6, 6);
        assertThat(map.indexOf(pair(1, 1)), is(-1));
        assertThat(map.indexOf(pair(2, 2)), is(0));
        assertThat(map.indexOf(pair(3, 3)), is(1));
        assertThat(map.indexOf(pair(4, 4)), is(2));
        assertThat(map.indexOf(pair(5, 5)), is(3));
        assertThat(map.indexOf(pair(6, 6)), is(4));
    }

    @Test
    public void canGetByIndex() throws Exception {
        PersistentSortedMap<Integer, Integer> map = sortedMap(4, 4, 5, 5, 3, 3, 2, 2, 6, 6);
        assertThat(map.get(0), is(pair(2,2)));
        assertThat(map.get(1), is(pair(3,3)));
        assertThat(map.get(2), is(pair(4,4)));
        assertThat(map.get(3), is(pair(5,5)));
        assertThat(map.get(4), is(pair(6,6)));
    }

    @Test
    public void canCalculateSize() throws Exception {
        assertThat(sortedMap(4, 4, 5, 5, 3, 3, 2, 2, 6, 6).size(), is(5));
    }

    @Test
    @Ignore("Manual")
    public void creatingASortedMapFromAnIterableIsFast() throws Exception {
        //in order - fold / cons Elapsed msecs for 11 runs:	Avg:13.460369555555555	Min:8.874518	Max:99.661891	Total:229.67973499999997
        //in order - sorted list Elapsed msecs for 11 runs:	Avg:11.016874222222222	Min:6.171428	Max:101.041891	Total:206.36518700000005
        // shuffle - fold / cons Elapsed msecs for 11 runs:	Avg:19.289151888888888	Min:13.316564	Max:113.912953	Total:300.831884
        // shuffle - sorted list Elapsed msecs for 11 runs:	Avg:15.702062000000002	Min:8.892117	Max:125.818735	Total:276.02941000000004
        final Seq<Integer> integers = range(0, 10000).safeCast(Integer.class).shuffle();
        TimeReport time = TimeReport.time(10, new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return sortedMap(integers.map(asPair()));
            }
        });
        System.out.println(time);
    }

    public static Function<Integer, Pair<Integer, Integer>> asPair() {
        return new Function<Integer, Pair<Integer, Integer>>() {
            @Override
            public Pair<Integer, Integer> call(Integer integer) throws Exception {
                return pair(integer, integer);
            }
        };
    }

    @Test
    public void canGetFirst() throws Exception {
        assertThat(sortedMap(4, "Alex", 1, "Dan", 3, "Stu", 2, "Ray").first(), is(pair(1, "Dan")));
    }

    @Test
    public void canRemoveFirst() throws Exception {
        final Pair<? extends PersistentSortedMap<Integer, String>, Pair<Integer, String>> result = sortedMap(4, "Alex", 1, "Dan", 3, "Stu", 2, "Ray").removeFirst();
        assertThat(result.first(), hasExactly(pair(2, "Ray"), pair(3, "Stu"), pair(4, "Alex")));
        assertThat(result.second(), is(pair(1, "Dan")));
    }

    @Test
    public void canGetLast() throws Exception {
        assertThat(sortedMap(4, "Alex", 1, "Dan", 3, "Stu", 2, "Ray").last(), is(pair(4, "Alex")));
    }

    @Test
    public void canRemoveLast() throws Exception {
        final Pair<? extends PersistentSortedMap<Integer, String>, Pair<Integer, String>> result = sortedMap(4, "Alex", 1, "Dan", 3, "Stu", 2, "Ray").removeLast();
        assertThat(result.first(), hasExactly(pair(1, "Dan"), pair(2, "Ray"), pair(3, "Stu")));
        assertThat(result.second(), is(pair(4, "Alex")));
    }

    @Test
    public void canRemove() throws Exception {
        final PersistentMap<Integer, String> map = sortedMap(4, "Alex", 1, "Dan", 3, "Stu", 2, "Ray");
        assertThat(map.delete(4), hasExactly(pair(1, "Dan"), pair(2, "Ray"), pair(3, "Stu")));
        assertThat(map.delete(3), hasExactly(pair(1, "Dan"), pair(2, "Ray"), pair(4, "Alex")));
        assertThat(map.delete(2), hasExactly(pair(1, "Dan"), pair(3, "Stu"), pair(4, "Alex")));
        assertThat(map.delete(1), hasExactly(pair(2, "Ray"), pair(3, "Stu"), pair(4, "Alex")));
        assertThat(map.delete(0), is(map));
    }

    @Test
    public void canPut() throws Exception {
        PersistentMap<Integer, String> map = sortedMap(1, "Dan").insert(3, "Stu").insert(2, "Ray");
        assertThat(map, hasExactly(pair(1, "Dan"), pair(2, "Ray"), pair(3, "Stu")));
    }

    @Test
    public void canCheckContains() throws Exception {
        PersistentMap<Integer, String> map = sortedMap(1, "Dan", 2, "Ray", 3, "Stu");
        assertThat(map.contains(2), is(true));
        assertThat(map.contains(4), is(false));
    }

    @Test
    public void supportsExists() throws Exception {
        PersistentMap<Integer, String> map = sortedMap(1, "Dan", 2, "Ray", 3, "Stu");
        assertThat(map.exists(Predicates.is(2)), is(true));
        assertThat(map.exists(Predicates.is(4)), is(false));
    }

    @Test
    public void canCreateATreeFromAnIterable() throws Exception {
        PersistentMap<Integer, String> map = sortedMap(sequence(pair(1, "Dan"), pair(2, "Ray"), pair(3, "Stu")));
        assertThat(map.contains(2), is(true));
        assertThat(map.contains(4), is(false));
    }

    @Test
    public void canConvertToPersistentList() throws Exception {
        PersistentList<Pair<Integer, String>> map = sortedMap(2, "Ray", 1, "Dan", 3, "Stu").toPersistentList();
        assertThat(map, hasExactly(pair(1, "Dan"), pair(2, "Ray"), pair(3, "Stu")));
    }

    @Test
    public void canJoin() throws Exception {
        PersistentMap<Integer, String> map = sortedMap(1, "Dan", 2, "Ray").joinTo(sortedMap(4, "Matt", 3, "Stu"));
        assertThat(map, hasExactly(pair(1, "Dan"), pair(2, "Ray"), pair(3, "Stu"), pair(4, "Matt")));
    }

    @Test
    public void canGet() throws Exception {
        PersistentMap<Integer, String> map = sortedMap(1, "Dan", 2, "Ray", 3, "Stu");
        assertThat(map.lookup(2), is(some("Ray")));
        assertThat(map.lookup(4), is(none(String.class)));
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
        assertThat(sortedMap("Dan", 2).map(add(2)), is(sortedMap("Dan", (Number) 4)));
    }
}
