package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Returns;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.callables.TimeCallable;
import com.googlecode.totallylazy.callables.TimeReport;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListMap;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.collections.PersistentSortedMapTest.asPair;
import static com.googlecode.totallylazy.matchers.IterablePredicates.startsWith;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.numbers.Numbers.range;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;

@Ignore("Manual Performance Tests")
public class MapPerformanceTest {
    public static final int SIZE = 100000;
    public static final int NUMBER_OF_CALLS = 500;
    public static final Sequence<Integer> range = range(1, SIZE).safeCast(Integer.class).realise();
    public static final Sequence<Integer> keys_ = range.shuffle().cycle().memorise();

    @Test
    public void iterateWorksOnLargeData() throws Exception {
        assertThat(createPersistent(range), startsWith(sequence(pair(1, 1), pair(2, 2), pair(3, 3))));
    }

    @Test
    public void getIsPrettyQuick() throws Exception {
        for (int i = 0; i < 10; i++) {
            System.out.println(TimeReport.time(NUMBER_OF_CALLS, persistentGet(createPersistent(range))));
            System.out.println(TimeReport.time(NUMBER_OF_CALLS, persistentGet(createHash(range))));
            System.out.println(TimeReport.time(NUMBER_OF_CALLS, mutableGet(createMutable(range, new HashMap<Integer, Integer>()))));
            System.out.println(TimeReport.time(NUMBER_OF_CALLS, mutableGet(createMutable(range, new java.util.TreeMap<Integer, Integer>()))));
            System.out.println(TimeReport.time(NUMBER_OF_CALLS, mutableGet(createMutable(range, new ConcurrentSkipListMap<Integer, Integer>(), "CSLMap "))));
            System.out.println("");
        }
    }

    @Test
    public void removeIsQuick() throws Exception {
        System.out.println("SIZE = " + SIZE);
        TimeReport hashMapReport = new TimeReport();
        TimeReport treeMapReport = new TimeReport();
        TimeReport cslMapReport = new TimeReport();
        TimeReport avlTreeReport = new TimeReport();

        Map<Integer, Integer> hashMap = createMutable(range, new HashMap<Integer, Integer>());
        Map<Integer, Integer> treeMap = createMutable(range, new java.util.TreeMap<Integer, Integer>());
        Map<Integer, Integer> cslMap = createMutable(range, new ConcurrentSkipListMap<Integer, Integer>(), "CSLMap ");
        PersistentMap<Integer, Integer> avlTree = createPersistent(range);

        for (int i = 0; i < 10; i++) {
            timeRemove(NUMBER_OF_CALLS, hashMap, hashMapReport);
            timeRemove(NUMBER_OF_CALLS, treeMap, treeMapReport);
            timeRemove(NUMBER_OF_CALLS, cslMap, cslMapReport);
            time(NUMBER_OF_CALLS, removePersistent(avlTree), avlTreeReport);
        }

        assertThat(hashMap.size(), is(SIZE));
        assertThat(treeMap.size(), is(SIZE));
        assertThat(cslMap.size(), is(SIZE));
        assertThat(avlTree.size(), is(SIZE));
        System.out.println();
        System.out.println("HashMap: " + hashMapReport);
        System.out.println("TreeMap: " + treeMapReport);
        System.out.println("CSLMap: " + cslMapReport);
        System.out.println("AvlTree: " + avlTreeReport);
    }

    @Test
    public void putIsQuick() throws Exception {
        System.out.println("SIZE = " + SIZE);
        TimeReport hashMapReport = new TimeReport();
        TimeReport treeMapReport = new TimeReport();
        TimeReport cslMapReport = new TimeReport();
        TimeReport avlTreeReport = new TimeReport();

        Map<Integer, Integer> hashMap = createMutable(range, new HashMap<Integer, Integer>());
        Map<Integer, Integer> treeMap = createMutable(range, new java.util.TreeMap<Integer, Integer>());
        Map<Integer, Integer> cslMap = createMutable(range, new ConcurrentSkipListMap<Integer, Integer>(), "CSLMap ");
        PersistentMap<Integer, Integer> avlTree = createHash(range);

        for (int i = 0; i < 10; i++) {
            timePut(NUMBER_OF_CALLS, hashMap, hashMapReport);
            timePut(NUMBER_OF_CALLS, treeMap, treeMapReport);
            timePut(NUMBER_OF_CALLS, cslMap, cslMapReport);
            time(NUMBER_OF_CALLS, persistentPut(avlTree), avlTreeReport);
        }

        assertThat(hashMap.size(), is(SIZE));
        assertThat(treeMap.size(), is(SIZE));
        assertThat(cslMap.size(), is(SIZE));
        assertThat(avlTree.size(), is(SIZE));
        System.out.println();
        System.out.println("HashMap: " + hashMapReport);
        System.out.println("TreeMap: " + treeMapReport);
        System.out.println("CSLMap: " + cslMapReport);
        System.out.println("AvlTree: " + avlTreeReport);
    }

    @SuppressWarnings("unchecked")
    private Callable<Object> persistentGet(final PersistentMap<Integer, Integer> map) {
        return new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return map.lookup(keys().head());
            }
        };
    }

    private Sequence<Integer> keys() {
        return keys_.forwardOnly();
    }

    @SuppressWarnings("unchecked")
    private Callable<Object> mutableGet(final Map<Integer, Integer> mutable) {
        return new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return Maps.get(mutable, keys().head());
            }
        };
    }

    private Map<Integer, Integer> createMutable(final Sequence<Integer> range, final Map<Integer, Integer> emptyMap) throws Exception {
        return createMutable(range, emptyMap, emptyMap.getClass().getSimpleName());
    }

    private Map<Integer, Integer> createMutable(Sequence<Integer> range, Map<Integer, Integer> emptyMap, String name) {
        System.out.print(name + ":\t");
        return range.fold(emptyMap, new Function2<Map<Integer, Integer>, Integer, Map<Integer, Integer>>() {
            @Override
            public Map<Integer, Integer> call(Map<Integer, Integer> map, Integer integer) throws Exception {
                map.put(integer, integer);
                return map;
            }
        });
    }

    private Callable<PersistentMap<Integer, Integer>> removePersistent(final PersistentMap<Integer, Integer> persistent) {
        return new Callable<PersistentMap<Integer, Integer>>() {
            @Override
            public PersistentMap<Integer, Integer> call() throws Exception {
                return persistent.delete(keys().head());
            }
        };
    }

    public static TimeReport time(int numberOfCalls, Callable<?> callable, final TimeReport report) {
        repeat(TimeCallable.time(callable, report)).take(numberOfCalls).realise();
        return report;
    }


    private TimeReport timeRemove(int count, final Map<Integer, Integer> map, final TimeReport report) {
        repeat(mutableRemove(map).time(report).then(putValueBack(map))).take(count).realise();
        return report;
    }

    private TimeReport timePut(int count, final Map<Integer, Integer> map, final TimeReport report) {
        repeat(mutablePut(map).time(report).then(remove(map))).take(count).realise();
        return report;
    }

    private Function<Integer, Integer> remove(final Map<Integer, Integer> map) {
        return new Function<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) throws Exception {
                return map.remove(integer);
            }
        };
    }

    private Function<Integer, Integer> putValueBack(final Map<Integer, Integer> map) {
        return new Function<Integer, Integer>() {
            @Override
            public Integer call(Integer key) throws Exception {
                return map.put(key, key);
            }
        };
    }

    private Returns<Integer> mutableRemove(final Map<Integer, Integer> map) {
        return new Returns<Integer>() {
            @Override
            public Integer call() throws Exception {
                return map.remove(keys().head());
            }
        };
    }

    private Callable<PersistentMap<Integer, Integer>> persistentPut(final PersistentMap<Integer, Integer> avlTree) {
        return new Callable<PersistentMap<Integer, Integer>>() {
            @Override
            public PersistentMap<Integer, Integer> call() throws Exception {
                Integer head = keys().head();
                return avlTree.insert(head, head);
            }
        };
    }

    private Returns<Integer> mutablePut(final Map<Integer, Integer> map) {
        return new Returns<Integer>() {
            @Override
            public Integer call() throws Exception {
                Integer head = SIZE + 1;
                map.put(head, head);
                return head;
            }
        };
    }

    public static PersistentSortedMap<Integer, Integer> createPersistent(final Sequence<Integer> range) throws Exception {
        PersistentSortedMap<Integer, Integer> map = PersistentSortedMap.constructors.sortedMap(range.map(asPair()));
        System.out.print("AVLTree:\t");
        return map;
    }

    public static PersistentMap<Integer, Integer> createHash(final Sequence<Integer> range) throws Exception {
        HashTreeMap<Integer, Integer> map = HashTreeMap.hashTreeMap(range.map(asPair()));
        System.out.print("HashTreeMap:\t");
        return map;
    }



}
