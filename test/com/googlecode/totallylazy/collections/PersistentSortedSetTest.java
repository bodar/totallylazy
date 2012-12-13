package com.googlecode.totallylazy.collections;

import org.junit.Test;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.collections.PersistentSortedSet.constructors.sortedSet;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PersistentSortedSetTest {
    @Test
    public void canInsertAnElement() throws Exception {
        PersistentSortedSet<Integer> actual = sortedSet(1).cons(2).cons(3);
        assertThat(actual, hasExactly(1,2,3));
    }

    @Test
    public void canCheckContains() throws Exception {
        PersistentSortedSet<Integer> sortedSet = sortedSet(2, 1, 3);
        assertThat(sortedSet.contains(2), is(true));
        assertThat(sortedSet.contains(4), is(false));
    }

    @Test
    public void canCreateATreeFromAnIterable() throws Exception {
        PersistentSortedSet<Integer> sortedSet = sortedSet(sequence(8, 6, 4, 1, 7, 3, 5));
        assertThat(sortedSet.contains(7), is(true));
        assertThat(sortedSet.contains(9), is(false));
    }

    @Test
    public void canConvertToPersistentList() throws Exception {
        PersistentList<Integer> tree = sortedSet(8, 6, 4, 1, 7, 3, 5).immutableList();
        assertThat(tree, hasExactly(1, 3, 4, 5, 6, 7, 8));
    }

    @Test
    public void isIterableAndOrdered() throws Exception {
        PersistentSortedSet<Integer> sortedSet = sortedSet(8, 6, 4, 1, 7, 3, 5);
        assertThat(sortedSet, hasExactly(1, 3, 4, 5, 6, 7, 8));
    }

    @Test
    public void isASet() throws Exception {
        PersistentSortedSet<Integer> sortedSet = sortedSet(3, 2, 1, 2, 1, 3);
        assertThat(sortedSet, hasExactly(1, 2, 3));
    }

    @Test
    public void canJoin() throws Exception {
        PersistentSortedSet<Integer> sortedSet = sortedSet(3, 2, 1, 2).joinTo(sortedSet(4, 1, 5, 2));
        assertThat(sortedSet, hasExactly(1, 2, 3, 4, 5));
    }

    @Test
    public void supportsHeadOption() {
        assertThat(sortedSet(1, 2, 3).headOption(), is(some(2)));
        PersistentSortedSet<Integer> empty = PersistentSortedSet.constructors.<Integer>sortedSet();
        assertThat(empty.headOption(), is(none(Integer.class)));
    }
}
