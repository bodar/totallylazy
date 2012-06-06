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
import java.util.TreeMap;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.collections.AVLTree.constructors.node;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.numbers.Numbers.range;
import static org.hamcrest.MatcherAssert.assertThat;

public class AVLTreeTest {
    @Test
    public void balancesRightRightCase() throws Exception {
        assertThat(node(3, null).put(4, null).put(5, null).toString(), is("(( 3 ) 4 ( 5 ))"));
    }

    @Test
    public void balancesRightLeftCase() throws Exception {
        assertThat(node(3, null).put(5, null).put(4, null).toString(), is("(( 3 ) 4 ( 5 ))"));
    }

    @Test
    public void balancesLeftLeftCase() throws Exception {
        assertThat(node(5, null).put(4, null).put(3, null).toString(), is("(( 3 ) 4 ( 5 ))"));
    }

    @Test
    public void balancesLeftRightCase() throws Exception {
        assertThat(node(5, null).put(3, null).put(4, null).toString(), is("(( 3 ) 4 ( 5 ))"));
    }

    @Test
    @Ignore("Manual")
    public void performanceDoesNotSuck() throws Exception {
//
//        BEGIN MUTABLE
//        Elapsed time: 1.323778 msecs
//        Elapsed msecs for 1001 runs:	Avg:5.320724094881408E-4	Min:3.48E-4	Max:0.021052	Total:0.5585599999999998
//        END MUTABLE
//        BEGIN IMMUTABLE
//        Elapsed time: 4.220562 msecs
//        Elapsed msecs for 1001 runs:	Avg:4.6022222222222205E-4	Min:2.06E-4	Max:0.013102	Total:0.49465499999999996
//        END IMMUTABLE

        
        final Sequence<Integer> range = range(1, 10000).safeCast(Integer.class).realise();
        final Sequence<Integer> keys = Randoms.between(0, 10000);


        for (int i = 0; i < 10; i++) {

            System.out.println("BEGIN MUTABLE");
            final Map<Integer, String> mutable = TimeCallable.time(new Callable<Map<Integer, String>>() {
                @Override
                public Map<Integer, String> call() throws Exception {
                    return range.fold(new TreeMap<Integer, String>() {{
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

            TimeReport time2 = TimeReport.time(1000, new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    Integer key = keys.head();
                    Option<String> value = Maps.get(mutable, key);
                    return value;
                }
            });
            System.out.println(time2);
            System.out.println("END MUTABLE");


            System.out.println("BEGIN IMMUTABLE");
            final ImmutableMap<Integer, String> map = TimeCallable.time(new Callable<ImmutableMap<Integer, String>>() {
                @Override
                public ImmutableMap<Integer, String> call() throws Exception {
                    return range.fold(node(0, "0"), new Callable2<ImmutableMap<Integer, String>, Integer, ImmutableMap<Integer, String>>() {
                        @Override
                        public ImmutableMap<Integer, String> call(ImmutableMap<Integer, String> node, Integer integer) throws Exception {
                            return node.put(integer, integer.toString());
                        }
                    });
                }
            }).call();

            TimeReport time = TimeReport.time(1000, new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    Integer key = keys.head();
                    Option<String> value = map.get(key);
                    return value;
                }
            });
            System.out.println(time);
            System.out.println("END IMMUTABLE");

        }


//        Files.write(("<html><head><style>td { text-align: center; border: 1px solid gray; }</style></head><body>" + new ImmutableMapRenderer().render((AVLTree<?, ?>) map) + "</body></html>").getBytes(), new File("/home/dev/tree.html"));
    }


    private class ImmutableMapRenderer {
        public String render(AVLTree<?, ?> map) {
            if (map instanceof AVLTree.Node) {
                return "<table>" + "<tr><td colspan='2'>" + ((AVLTree.Node) map).key + "</td></tr>" +
                        "<tr><td  valign='top'>" + render(((AVLTree.Node) map).left) + "</td><td valign='top'>" + render(((AVLTree.Node) map).right) + "</td></tr></table>";
            }
            return "";
        }
    }
}
