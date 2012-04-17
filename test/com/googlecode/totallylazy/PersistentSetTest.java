package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.UnbalancedSet.empty;
import static com.googlecode.totallylazy.UnbalancedSet.node;
import static com.googlecode.totallylazy.UnbalancedSet.set;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PersistentSetTest {
    @Test
    public void canInsertAnElement() throws Exception {
        PersistentSet<Integer> actual = set(2).cons(1).cons(3);
        PersistentSet<Integer> expected = node(2, node(1, empty(Integer.class), empty(Integer.class)), node(3, empty(Integer.class), empty(Integer.class)));
        assertThat(actual, is(expected));
    }

    @Test
    public void canCheckContains() throws Exception {
        PersistentSet<Integer> set = set(2, 1, 3);
        assertThat(set.contains(2), is(true));
        assertThat(set.contains(4), is(false));
    }

    @Test
    public void canCreateATreeFromAnIterable() throws Exception {
        PersistentSet<Integer> set = set(sequence(8, 6, 4, 1, 7, 3, 5));
        assertThat(set.contains(7), is(true));
        assertThat(set.contains(9), is(false));
    }

    @Test
    public void canConvertToPersistentList() throws Exception {
        PersistentList<Integer> tree = set(8, 6, 4, 1, 7, 3, 5).persistentList();
        assertThat(tree, hasExactly(1, 3, 4, 5, 6, 7, 8));
    }

    @Test
    public void isIterableAndOrdered() throws Exception {
        PersistentSet<Integer> set = set(8, 6, 4, 1, 7, 3, 5);
        assertThat(set, hasExactly(1, 3, 4, 5, 6, 7, 8));
    }

    @Test
    public void isASet() throws Exception {
        PersistentSet<Integer> set = set(3, 2, 1, 2, 1, 3);
        assertThat(set, hasExactly(1, 2, 3));
    }

    @Test
    public void canJoin() throws Exception {
        PersistentSet<Integer> set = set(3, 2, 1, 2).join(set(4, 1, 5, 2));
        assertThat(set, hasExactly(1, 2, 3, 4, 5));
    }
}
