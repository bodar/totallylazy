package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Files;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.callables.TimeReport;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

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

    static final Sequence<Integer> range = range(0, 100000).safeCast(Integer.class).realise();
    static final Sequence<Integer> keys_ = range.shuffle().cycle().memorise();

    @Test
    @Ignore("Manual")
    public void getIsPrettyQuick() throws Exception {
        for (int i = 0; i < 100; i++) {
            System.out.println(TimeReport.time(10000, mutableGet(createMutable(range, new HashMap<Integer, String>()))));
            System.out.println(TimeReport.time(10000, mutableGet(createMutable(range, new java.util.TreeMap<Integer, String>()))));
            System.out.println(TimeReport.time(10000, mutableGet(createMutable(range, new ConcurrentSkipListMap<Integer, String>(), "CSLMap "))));
            System.out.println(TimeReport.time(10000, immutableGet(createImmutable(range))));
            System.out.println("");
        }
    }

    @SuppressWarnings("unchecked")
    private Callable<Object> immutableGet(final ImmutableMap<Integer, String> map) {
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
    private Callable<Object> mutableGet(final Map<Integer, String> mutable) {
        return new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return Maps.get(mutable, keys().head());
            }
        };
    }

    private Map<Integer, String> createMutable(final Sequence<Integer> range, final Map<Integer, String> emptyMap) throws Exception {
        return createMutable(range, emptyMap, emptyMap.getClass().getSimpleName());
    }

    private Map<Integer, String> createMutable(Sequence<Integer> range, Map<Integer, String> emptyMap, String name) {
        System.out.print(name + ":\t");
        return range.fold(emptyMap, new Callable2<Map<Integer, String>, Integer, Map<Integer, String>>() {
            @Override
            public Map<Integer, String> call(Map<Integer, String> map, Integer integer) throws Exception {
                map.put(integer, integer.toString());
                return map;
            }
        });
    }

    @Test
    @Ignore
    public void removeIsQuick() throws Exception {
        for (int i = 0; i < 100; i++) {

            System.out.println(TimeReport.time(10000, mutableRemove(createMutable(range, new HashMap<Integer, String>()))));
            System.out.println(TimeReport.time(10000, mutableRemove(createMutable(range, new java.util.TreeMap<Integer, String>()))));
            System.out.println(TimeReport.time(10000, mutableRemove(createMutable(range, new ConcurrentSkipListMap<Integer, String>(), "CSLMap "))));

            final ImmutableMap<Integer, String> immutable = createImmutable(range);
            System.out.println(TimeReport.time(10000, new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return immutable.remove(keys().head());
                }
            }));

            System.out.println("");
        }
    }

    private Callable<Object> mutableRemove(final Map<Integer, String> map) {
        return new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return map.remove(keys().head());
            }
        };
    }

    @Test
    @Ignore
    public void putIsQuick() throws Exception {
        for (int i = 0; i < 100; i++) {
            System.out.println(TimeReport.time(10000, mutablePut(createMutable(range, new HashMap<Integer, String>()))));
            System.out.println(TimeReport.time(10000, mutablePut(createMutable(range, new java.util.TreeMap<Integer, String>()))));
            System.out.println(TimeReport.time(10000, mutablePut(createMutable(range, new ConcurrentSkipListMap<Integer, String>(), "CSLMap "))));

            final ImmutableMap<Integer, String> immutable = createImmutable(range);
            System.out.println(TimeReport.time(10000, new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return immutable.put(keys().head(), "Hello");
                }
            }));

            System.out.println();
        }
    }

    private Callable<Object> mutablePut(final Map<Integer, String> map) {
        return new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return map.put(keys().head(), "Hello");
            }
        };
    }

    private ImmutableSortedMap<Integer, String> createImmutable(final Sequence<Integer> range) throws Exception {
        ImmutableSortedMap<Integer, String> map = ImmutableSortedMap.constructors.sortedMap(range.map(asPair()));
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
