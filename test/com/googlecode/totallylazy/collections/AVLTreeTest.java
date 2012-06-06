package com.googlecode.totallylazy.collections;

import org.junit.Test;

import static com.googlecode.totallylazy.collections.AVLTree.constructors.node;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AVLTreeTest {
    @Test
    public void balances() throws Exception {
        ImmutableMap<Integer, Object> map = node(3, null).put(4, null).put(5, null);
        assertThat(map.toString(), is("(( 3 ) 4 ( 5 ))"));
    }
}
