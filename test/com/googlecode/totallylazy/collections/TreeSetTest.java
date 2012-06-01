package com.googlecode.totallylazy.collections;

import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.collections.ImmutableSet.constructors.set;
import static com.googlecode.totallylazy.collections.TreeSet.tree;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TreeSetTest {
    @Test
    public void canInsertAnElement() throws Exception {
        ImmutableSet<Integer> actual = tree(1).cons(2).cons(3);
        assertThat(actual, hasExactly(1,2,3));
    }

    @Test
    public void canCheckContains() throws Exception {
        ImmutableSet<Integer> set = set(2, 1, 3);
        assertThat(set.contains(2), is(true));
        assertThat(set.contains(4), is(false));
    }

    @Test
    public void canCreateATreeFromAnIterable() throws Exception {
        ImmutableSet<Integer> set = set(sequence(8, 6, 4, 1, 7, 3, 5));
        assertThat(set.contains(7), is(true));
        assertThat(set.contains(9), is(false));
    }

    @Test
    public void canConvertToPersistentList() throws Exception {
        ImmutableList<Integer> tree = set(8, 6, 4, 1, 7, 3, 5).persistentList();
        assertThat(tree, hasExactly(1, 3, 4, 5, 6, 7, 8));
    }

    @Test
    public void isIterableAndOrdered() throws Exception {
        ImmutableSet<Integer> set = set(8, 6, 4, 1, 7, 3, 5);
        assertThat(set, hasExactly(1, 3, 4, 5, 6, 7, 8));
    }

    @Test
    public void isASet() throws Exception {
        ImmutableSet<Integer> set = set(3, 2, 1, 2, 1, 3);
        assertThat(set, hasExactly(1, 2, 3));
    }

    @Test
    public void canJoin() throws Exception {
        ImmutableSet<Integer> set = set(3, 2, 1, 2).join(set(4, 1, 5, 2));
        assertThat(set, hasExactly(1, 2, 3, 4, 5));
    }
}
