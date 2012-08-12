package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Files;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.callables.TimeCallable;
import com.googlecode.totallylazy.callables.TimeReport;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListMap;

import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.collections.AVLTree.constructors.avlTree;
import static com.googlecode.totallylazy.collections.ImmutableSortedMapTest.asPair;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.numbers.Numbers.range;
import static org.hamcrest.MatcherAssert.assertThat;

public class AVLTreeTest {
    @Test
    public void balancesRightRightCase() throws Exception {
        assertThat(avlTree(3, null).put(4, null).put(5, null).toString(), is("(( 3 ) 4 ( 5 ))"));
    }

    @Test
    public void balancesRightLeftCase() throws Exception {
        assertThat(avlTree(3, null).put(5, null).put(4, null).toString(), is("(( 3 ) 4 ( 5 ))"));
    }

    @Test
    public void balancesLeftLeftCase() throws Exception {
        assertThat(avlTree(5, null).put(4, null).put(3, null).toString(), is("(( 3 ) 4 ( 5 ))"));
    }

    @Test
    public void balancesLeftRightCase() throws Exception {
        assertThat(avlTree(5, null).put(3, null).put(4, null).toString(), is("(( 3 ) 4 ( 5 ))"));
    }

    @Test
    public void balancesDeletion() throws Exception {
        final ImmutableMap<Integer, Object> map = avlTree(0, null).put(1, null).put(2, null).put(3, null).put(4, null).put(5, null).put(6, null);
        assertThat(map.remove(3).toString(), is("((( 0 ) 1 ) 2 (( 4 ) 5 ( 6 )))"));
    }

    public static final int SIZE = 100000;
    public static final int NUMBER_OF_CALLS = 50000;
    static final Sequence<Integer> range = range(1, SIZE).safeCast(Integer.class).realise();
    static final Sequence<Integer> keys_ = range.shuffle().cycle().memorise();

    @Test
    @Ignore("Manual")
    public void getIsPrettyQuick() throws Exception {
        for (int i = 0; i < 10; i++) {
            System.out.println(TimeReport.time(NUMBER_OF_CALLS, immutableGet(createImmutable(range))));
            System.out.println(TimeReport.time(NUMBER_OF_CALLS, mutableGet(createMutable(range, new HashMap<Integer, Integer>()))));
            System.out.println(TimeReport.time(NUMBER_OF_CALLS, mutableGet(createMutable(range, new java.util.TreeMap<Integer, Integer>()))));
            System.out.println(TimeReport.time(NUMBER_OF_CALLS, mutableGet(createMutable(range, new ConcurrentSkipListMap<Integer, Integer>(), "CSLMap "))));
            System.out.println("");
        }
    }

    @SuppressWarnings("unchecked")
    private Callable<Object> immutableGet(final ImmutableMap<Integer, Integer> map) {
        return new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return map.get(keys().head());
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
        return range.fold(emptyMap, new Callable2<Map<Integer, Integer>, Integer, Map<Integer, Integer>>() {
            @Override
            public Map<Integer, Integer> call(Map<Integer, Integer> map, Integer integer) throws Exception {
                map.put(integer, integer);
                return map;
            }
        });
    }

    @Test
    @Ignore
    public void removeIsQuick() throws Exception {
        System.out.println("SIZE = " + SIZE);
        TimeReport hashMapReport = new TimeReport();
        TimeReport treeMapReport = new TimeReport();
        TimeReport cslMapReport = new TimeReport();
        TimeReport avlTreeReport = new TimeReport();

        Map<Integer, Integer> hashMap = createMutable(range, new HashMap<Integer, Integer>());
        Map<Integer, Integer> treeMap = createMutable(range, new java.util.TreeMap<Integer, Integer>());
        Map<Integer, Integer> cslMap = createMutable(range, new ConcurrentSkipListMap<Integer, Integer>(), "CSLMap ");
        ImmutableMap<Integer, Integer> avlTree = createImmutable(range);

        for (int i = 0; i < 100; i++) {
            timeRemove(NUMBER_OF_CALLS, hashMap, hashMapReport);
            timeRemove(NUMBER_OF_CALLS, treeMap, treeMapReport);
            timeRemove(NUMBER_OF_CALLS, cslMap, cslMapReport);
            time(NUMBER_OF_CALLS, removeImmutable(avlTree), avlTreeReport);
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

    private Callable<ImmutableMap<Integer, Integer>> removeImmutable(final ImmutableMap<Integer, Integer> immutable) {
        return new Callable<ImmutableMap<Integer, Integer>>() {
            @Override
            public ImmutableMap<Integer, Integer> call() throws Exception {
                return immutable.remove(keys().head());
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

    private Callable1<Integer, Integer> remove(final Map<Integer, Integer> map) {
        return new Callable1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) throws Exception {
                return map.remove(integer);
            }
        };
    }

    private Callable1<Integer, Integer> putValueBack(final Map<Integer, Integer> map) {
        return new Callable1<Integer, Integer>() {
            @Override
            public Integer call(Integer key) throws Exception {
                return map.put(key, key);
            }
        };
    }

    private Function<Integer> mutableRemove(final Map<Integer, Integer> map) {
        return new Function<Integer>() {
            @Override
            public Integer call() throws Exception {
                return map.remove(keys().head());
            }
        };
    }

    @Test
    @Ignore
    public void putIsQuick() throws Exception {
            System.out.println("SIZE = " + SIZE);
            TimeReport hashMapReport = new TimeReport();
            TimeReport treeMapReport = new TimeReport();
            TimeReport cslMapReport = new TimeReport();
            TimeReport avlTreeReport = new TimeReport();

            Map<Integer, Integer> hashMap = createMutable(range, new HashMap<Integer, Integer>());
            Map<Integer, Integer> treeMap = createMutable(range, new java.util.TreeMap<Integer, Integer>());
            Map<Integer, Integer> cslMap = createMutable(range, new ConcurrentSkipListMap<Integer, Integer>(), "CSLMap ");
            ImmutableMap<Integer, Integer> avlTree = createImmutable(range);

            for (int i = 0; i < 100; i++) {
                timePut(NUMBER_OF_CALLS, hashMap, hashMapReport);
                timePut(NUMBER_OF_CALLS, treeMap, treeMapReport);
                timePut(NUMBER_OF_CALLS, cslMap, cslMapReport);
                time(NUMBER_OF_CALLS, immutablePut(avlTree), avlTreeReport);
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

    private Callable<ImmutableMap<Integer, Integer>> immutablePut(final ImmutableMap<Integer, Integer> avlTree) {
        return new Callable<ImmutableMap<Integer, Integer>>() {
            @Override
            public ImmutableMap<Integer, Integer> call() throws Exception {
                Integer head = keys().head();
                return avlTree.put(head, head);
            }
        };
    }

    private Function<Integer> mutablePut(final Map<Integer, Integer> map) {
        return new Function<Integer>() {
            @Override
            public Integer call() throws Exception {
                Integer head = SIZE + 1;
                map.put(head, head);
                return head;
            }
        };
    }

    private ImmutableSortedMap<Integer, Integer> createImmutable(final Sequence<Integer> range) throws Exception {
        ImmutableSortedMap<Integer, Integer> map = ImmutableSortedMap.constructors.sortedMap(range.map(asPair()));
        System.out.print("AVLTree:\t");
        return map;
    }

    @Test
    @Ignore("Manual")
    public void canVisualiseTree() throws Exception {
        render((TreeMap<?, ?>) createImmutable(range));
    }

    private void render(TreeMap<?, ?> map) {
        final File file = new File(Files.temporaryDirectory(), getClass().getSimpleName() + ".html");
        Files.write(("<html><head><style>" +
                ".tree { border: 1px solid grey; padding: 0 1px; } " +
                ".key { text-align: center; } " +
                ".tree, .left, .right { display: table-cell; }" +
                "</style></head><body>" + new TreeMapRenderer().render(map) + "</body></html>").getBytes(), file);
        System.out.println("tree = " + file);
    }

    private class TreeMapRenderer {
        public String render(TreeMap<?, ?> map) {
            if (map.isEmpty()) return "";
            return "<div class='tree'>" +
                    "<div class='key'>" + map.key() + "</div>" +
                    "<div class='left'>" + render(map.left()) + "</div>" +
                    "<div class='right'>" + render(map.right()) + "</div>" +
                    "</div>";
        }
    }
}
