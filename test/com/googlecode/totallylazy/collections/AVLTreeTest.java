package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import org.junit.Test;

import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.collections.AVLTree.constructors.avlTree;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AVLTreeTest extends MapContract {
    @Override
    protected <K extends Comparable<K>, V> MapFactory<K, V, ? extends PersistentMap<K, V>> factory() {
        return AVLTree.constructors.<K, V>factory();
    }

    @Test
    public void balancesRightRightCase() throws Exception {
        System.out.println(map("C", 1, "B", 2, "A", 3).toString());
        assertThat(map(3, null, 4, null, 5, null).toString(), is("(( 3=null ) 4=null ( 5=null ))"));
    }

    @Test
    public void balancesRightLeftCase() throws Exception {
        assertThat(map(3, null, 5, null, 4, null).toString(), is("(( 3=null ) 4=null ( 5=null ))"));
    }

    @Test
    public void balancesLeftLeftCase() throws Exception {
        assertThat(map(5, null, 4, null, 3, null).toString(), is("(( 3=null ) 4=null ( 5=null ))"));
    }

    @Test
    public void balancesLeftRightCase() throws Exception {
        assertThat(avlTree(5, null).insert(3, null).insert(4, null).toString(), is("(( 3=null ) 4=null ( 5=null ))"));
    }

    @Test
    public void balancesDeletion() throws Exception {
        final PersistentMap<Integer, Object> map = map(0, null, 1, null, 2, null, 3, null, 4, null).insert(5, null).insert(6, null);
        assertThat(map.delete(3).toString(), is("((( 0=null ) 1=null ) 2=null (( 4=null ) 5=null ( 6=null )))"));
    }

    @Test
    public void supportsHeadOption() {
        assertThat(map(1, "A").
                cons(pair(2, "B")).
                cons(pair(3, "C")).headOption(),
                is(some(pair(2, "B"))));

        assertThat(
                empty(Integer.class, String.class).headOption(),
                is(Option.<Pair<Integer, String>>none()));
    }
}
