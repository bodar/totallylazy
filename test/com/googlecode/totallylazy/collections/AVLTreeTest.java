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
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.numbers.Numbers.range;
import static org.hamcrest.MatcherAssert.assertThat;

public class AVLTreeTest {
    private Sequence<Integer> integers(final int start, final int end) {
        return range(start, end).safeCast(Integer.class);
    }

    @Test
    public void canGetByIndex() throws Exception {
        AVLTree<Integer, Integer> map = avlTree(4, 4).put(5, 5).put(3, 3).put(2, 2).put(6, 6);
        assertThat(map.index(0).first(), is(2));
        assertThat(map.index(1).first(), is(3));
        assertThat(map.index(2).first(), is(4));
        assertThat(map.index(3).first(), is(5));
        assertThat(map.index(4).first(), is(6));
    }

    @Test
    public void canCalculateSize() throws Exception {
        assertThat(avlTree(4, null).put(5, null).put(3, null).put(2, null).put(6, null).size(), is(5));
    }

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

    final Sequence<Integer> range = range(1, 10000).safeCast(Integer.class).realise();
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

        for (int i = 0; i < 10; i++) {
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


        final ImmutableMap<Integer, String> map = TimeCallable.time(new Callable<ImmutableMap<Integer, String>>() {
            @Override
            public ImmutableMap<Integer, String> call() throws Exception {
                return range.fold(avlTree(0, "0"), new Callable2<ImmutableMap<Integer, String>, Integer, ImmutableMap<Integer, String>>() {
                    @Override
                    public ImmutableMap<Integer, String> call(ImmutableMap<Integer, String> node, Integer integer) throws Exception {
                        return node.put(integer, integer.toString());
                    }
                });
            }
        }).call();


        render((AVLTree<?, ?>) map);
    }

    private Map<Integer, String> createMutable(final Sequence<Integer> range) throws Exception {
        return TimeCallable.time(new Callable<Map<Integer, String>>() {
            @Override
            public Map<Integer, String> call() throws Exception {
                return range.fold(new ConcurrentSkipListMap<Integer, String>() {{
                                      put(0, "0");
                                  }}, new Callable2<Map<Integer, String>, Integer, Map<Integer, String>>() {
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

    private ImmutableMap<Integer, String> createImmutable(final Sequence<Integer> range) throws Exception {
        return TimeCallable.time(new Callable<ImmutableMap<Integer, String>>() {
            @Override
            public ImmutableMap<Integer, String> call() throws Exception {
                return range.fold(avlTree(0, "0"), new Callable2<ImmutableMap<Integer, String>, Integer, ImmutableMap<Integer, String>>() {
                    @Override
                    public ImmutableMap<Integer, String> call(ImmutableMap<Integer, String> node, Integer integer) throws Exception {
                        return node.put(integer, integer.toString());
                    }
                });
            }
        }).call();
    }

    private void render(TreeMap<?, ?> map) {
        final File file = new File(Files.temporaryDirectory(), getClass().getSimpleName() + ".html");
        Files.write(("<html><head><style>td { text-align: center; border: 1px solid gray; }</style></head><body>" + new ImmutableMapRenderer().render(map) + "</body></html>").getBytes(), file);
        System.out.println("tree = " + file);
    }


    private class ImmutableMapRenderer {
        public String render(TreeMap<?, ?> map) {
            if(map.isEmpty()) return "";
                return "<table>" + "<tr><td colspan='2'>" + map.key() + "</td></tr>" +
                        "<tr><td  valign='top'>" + render(map.left()) + "</td><td valign='top'>" + render(map.right()) + "</td></tr></table>";
        }
    }
}
