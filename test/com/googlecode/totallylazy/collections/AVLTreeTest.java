package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Files;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.callables.TimeCallable;
import com.googlecode.totallylazy.callables.TimeReport;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListMap;

import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.bytes;
import static com.googlecode.totallylazy.collections.AVLTree.constructors.avlTree;
import static com.googlecode.totallylazy.collections.PersistentSortedMapTest.asPair;
import static com.googlecode.totallylazy.matchers.IterableMatcher.startsWith;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.numbers.Numbers.range;
import static org.hamcrest.MatcherAssert.assertThat;

public class AVLTreeTest extends MapContract {
    @Override
    protected <K extends Comparable<K>, V> MapFactory<K, V, ? extends PersistentMap<K, V>> factory() {
        return AVLTree.constructors.<K,V>factory();
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
        final PersistentMap<Integer, Object> map = avlTree(0, null).put(1, null).put(2, null).put(3, null).put(4, null).put(5, null).put(6, null);
        assertThat(map.remove(3).toString(), is("((( 0 ) 1 ) 2 (( 4 ) 5 ( 6 )))"));
    }

    @Test
    public void supportsHeadOption() {
        assertThat(avlTree(1, "A").
                cons(pair(2, "B")).
                cons(pair(3, "C")).headOption(),
                is(some(pair(2, "B"))));

        AVLTree<Integer, String> empty = AVLTree.constructors.factory.create(null);
        assertThat(
                empty.headOption(),
                is(Option.<Pair<Integer, String>>none()));
    }

    @Test
    public void canIterate() throws Exception {
        final Iterator<Pair<Integer, Integer>> iterator = avlTree(0, 0).put(1, 1).put(2, 2).iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next().first(), is(0));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next().first(), is(1));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next().first(), is(2));
        assertThat(iterator.hasNext(), is(false));
    }
}
