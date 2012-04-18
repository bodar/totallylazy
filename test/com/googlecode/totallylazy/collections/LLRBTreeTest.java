package com.googlecode.totallylazy.collections;

import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.collections.EmptyTree.empty;
import static com.googlecode.totallylazy.collections.LLRBTree.black;
import static com.googlecode.totallylazy.collections.LLRBTree.red;
import static com.googlecode.totallylazy.collections.LLRBTree.set;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LLRBTreeTest {
    @Test
    public void leftBalances() throws Exception {
        PersistentSet<Integer> blackRed = set(3).cons(2);
        assertThat(blackRed, is(black(emptyRed(2), 3, empty(Integer.class))));
        PersistentSet<Integer> actual = blackRed.cons(1);
        assertThat(actual, is(red(emptyBlack(1), 2, emptyBlack(3))));
    }

    @Test
    public void rightBalancesExample1() throws Exception {
        PersistentSet<Integer> blackRed = set(2).cons(1);
        assertThat(blackRed, is(black(emptyRed(1), 2, empty(Integer.class))));
        PersistentSet<Integer> actual = blackRed.cons(3);
        assertThat(actual, is(red(emptyBlack(1), 2, emptyBlack(3))));
    }

    @Test
    public void rightBalancesExample2() throws Exception {
        PersistentSet<Integer> blackRedRight = black(emptyBlack(1), 2, emptyRed(3));
        PersistentSet<Integer> balanced = LLRBTree.balanceRight((TreeNode<Integer>) blackRedRight);

        PersistentSet<Integer> black = black(red(emptyBlack(1), 2, empty(Integer.class)), 3, empty(Integer.class));
        assertThat(balanced, is(black));
    }

    private <T extends Comparable<? super T>> PersistentSet<T> emptyBlack(final T value) {
        return black(EmptyTree.<T>empty(), value, EmptyTree.<T>empty());
    }

    private <T extends Comparable<? super T>> PersistentSet<T> emptyRed(final T value) {
        return red(EmptyTree.<T>empty(), value, EmptyTree.<T>empty());
    }

    @Test
    public void canInsertAnElement() throws Exception {
        PersistentSet<Integer> actual = set(1).cons(2).cons(3);
        assertThat(actual, is(red(emptyBlack(1), 2, emptyRed(3))));
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
