package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Files;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Randoms;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.callables.TimeCallable;
import com.googlecode.totallylazy.callables.TimeReport;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;
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

    final Sequence<Integer> range = range(0, 10000).safeCast(Integer.class).realise();
    final Sequence<Integer> keys = Randoms.between(0, 10000);

    @Test
    @Ignore("Manual")
    public void getIsPrettyQuick() throws Exception {
//
//        BEGIN MUTABLE
//        Elapsed time: 1.323778 msecs
//        Elapsed msecs for 1001 runs:	Avg:5.320724094881408E-4	Min:3.48E-4	Max:0.021052	Total:0.5585599999999998
//        END MUTABLE
//        BEGIN IMMUTABLE
//        Elapsed time: 4.220562 msecs
//        Elapsed msecs for 1001 runs:	Avg:4.6022222222222205E-4	Min:2.06E-4	Max:0.013102	Total:0.49465499999999996
//        END IMMUTABLE

        for (int i = 0; i < 100; i++) {
            System.out.println("BEGIN MUTABLE");
            final Map<Integer, String> mutable = createMutable(range);
            System.out.println(TimeReport.time(1000, new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    Integer key = keys.head();
                    Option<String> value = Maps.get(mutable, key);
                    return value;
                }
            }));
            System.out.println("END MUTABLE");

            System.out.println("BEGIN IMMUTABLE");
            final ImmutableMap<Integer, String> map = createImmutable(range);
            System.out.println(TimeReport.time(1000, new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    Integer key = keys.head();
                    Option<String> value = map.get(key);
                    return value;
                }
            }));
            System.out.println("END IMMUTABLE");

        }
    }

    private Map<Integer, String> createMutable(final Sequence<Integer> range) throws Exception {
        return TimeCallable.time(new Callable<Map<Integer, String>>() {
            @Override
            public Map<Integer, String> call() throws Exception {
                return range.fold(new ConcurrentSkipListMap<Integer, String>(), new Callable2<Map<Integer, String>, Integer, Map<Integer, String>>() {
                    @Override
                    public Map<Integer, String> call(Map<Integer, String> map, Integer integer) throws Exception {
                        map.put(integer, integer.toString());
                        return map;
                    }
                }
                );
            }
        }).call();
    }

    @Test
    @Ignore
    public void removeIsQuick() throws Exception {
        for (int i = 0; i < 10; i++) {

            System.out.println("BEGIN MUTABLE");
            final Map<Integer, String> map = createMutable(range);
            System.out.println(TimeReport.time(1000, new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return map.remove(keys.head());
                }
            }));
            System.out.println("END MUTABLE");

            System.out.println("BEGIN IMMUTABLE");
            final ImmutableMap<Integer, String> immutable = createImmutable(range);
            System.out.println(TimeReport.time(1000, new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return immutable.remove(keys.head());
                }
            }));
            System.out.println("END IMMUTABLE");

        }
    }

    private ImmutableSortedMap<Integer, String> createImmutable(final Sequence<Integer> range) throws Exception {
        return TimeCallable.time(new Callable<ImmutableSortedMap<Integer, String>>() {
            @Override
            public ImmutableSortedMap<Integer, String> call() throws Exception {
                return ImmutableSortedMap.constructors.sortedMap(range.map(asPair()));
            }
        }).call();
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