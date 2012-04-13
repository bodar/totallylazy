package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.PersistentTree.empty;
import static com.googlecode.totallylazy.PersistentTree.node;
import static com.googlecode.totallylazy.PersistentTree.tree;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PersistentTreeTest {
    @Test
    public void canInsertAnElement() throws Exception {
        PersistentTree<Integer> actual = tree(2).insert(1).insert(3);
        PersistentTree<Integer> expected = node(2, node(1, empty(Integer.class), empty(Integer.class)), node(3, empty(Integer.class), empty(Integer.class)));
        assertThat(actual, is(expected));
    }

    @Test
    public void canCheckContains() throws Exception {
        PersistentTree<Integer> tree = tree(2).insert(1).insert(3);
        assertThat(tree.contains(2), is(true));
        assertThat(tree.contains(4), is(false));
    }

    @Test
    public void canCreateATreeFromAnIterable() throws Exception {
        PersistentTree<Integer> tree = tree(sequence(8, 6, 4, 1, 7, 3, 5));
        assertThat(tree.contains(7), is(true));
        assertThat(tree.contains(9), is(false));
    }

    @Test
    public void canCreateATreeVarArgs() throws Exception {
        PersistentTree<Integer> tree = tree(8, 6, 4, 1, 7, 3, 5);
        assertThat(tree.contains(7), is(true));
        assertThat(tree.contains(9), is(false));
    }

    @Test
    public void canConvertToPersistentList() throws Exception {
        PersistentList<Integer> tree = tree(8, 6, 4, 1, 7, 3, 5).persistentList();
        assertThat(tree, hasExactly(1, 3, 4, 5, 6, 7, 8));
    }

    @Test
    public void canIterate() throws Exception {
        PersistentTree<Integer> tree = tree(8, 6, 4, 1, 7, 3, 5);
        assertThat(tree, hasExactly(1, 3, 4, 5, 6, 7, 8));
    }
}
