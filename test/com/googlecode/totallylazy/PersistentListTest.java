package com.googlecode.totallylazy;

import com.googlecode.totallylazy.iterators.ReadOnlyListIterator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.googlecode.totallylazy.PersistentList.list;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class PersistentListTest {
    @Test
    public void supportsOneElement() throws Exception {
        assertThat(list(1), hasExactly(1));
    }

    @Test
    public void supportsTwoElements() throws Exception {
        assertThat(list(1, 2), hasExactly(1, 2));
    }

    @Test
    public void supportsThreeElements() throws Exception {
        assertThat(list(1, 2, 3), hasExactly(1, 2, 3));
    }

    @Test
    public void supportsFourElements() throws Exception {
        assertThat(list(1, 2, 3, 4), hasExactly(1, 2, 3, 4));
    }

    @Test
    public void supportsFiveElements() throws Exception {
        assertThat(list(1, 2, 3, 4, 5), hasExactly(1, 2, 3, 4, 5));
    }

    @Test
    public void supportsVarArgsForMoreThanFive() throws Exception {
        assertThat(list(1, 2, 3, 4, 5, 6), hasExactly(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void supportsRemoveAll() throws Exception {
        assertThat(list(1, 2, 3, 4, 5, 6).removeAll(sequence(3, 4)), hasExactly(1, 2, 5, 6));
    }

    @Test
    public void supportsRemove() throws Exception {
        assertThat(list(1, 2, 3, 4, 5, 6).remove(3), hasExactly(1, 2, 4, 5, 6));
    }

    @Test
    public void supportsEquality() throws Exception {
        assertThat(list(1, 2, 3, 4, 5, 6), is(list(1, 2, 3, 4, 5, 6)));
        assertThat(list(1, 2, 3, 4, 5, 6), not(list(1, 2, 3, 4, 5)));
    }

    @Test
    public void supportsSize() throws Exception {
        assertThat(list(1, 2, 3, 4, 5, 6).size(), is(6));
        assertThat(list(1, 2, 3, 4, 5).size(), is(5));
    }

    @Test
    public void supportsAdd() throws Exception {
        assertThat(list(1).add(2), hasExactly(1, 2));
    }

    @Test
    public void supportsCons() throws Exception {
        assertThat(list(1).cons(2), hasExactly(2, 1));
    }

    @Test
    public void supportsToList() throws Exception {
        final List<Integer> actual = list(1).toList();
        final List<Integer> expected = new ArrayList<Integer>() {{
            add(1);
        }};
        assertThat(actual, is(expected));
    }

    @Test
    public void supportIterator() throws Exception {
        final Iterator<Integer> iterator = list(1, 2, 3).iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(1));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(2));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(3));
        assertThat(iterator.hasNext(), is(false));
    }
}
