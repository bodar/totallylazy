package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.callables.TimeReport;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.collections.ImmutableList.constructors.list;
import static com.googlecode.totallylazy.collections.ImmutableSortedSet.constructors.sortedSet;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.matchers.NumberMatcher.startsWith;
import static com.googlecode.totallylazy.numbers.Numbers.range;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class PersistentListTest {
    @Test
    public void canLookupIndexOf() throws Exception {
        assertThat(list("Dan", "Matt").indexOf("Dan"), is(0));
        assertThat(list("Dan", "Matt").indexOf("Matt"), is(1));
    }

    @Test
    public void canLookupByIndex() throws Exception {
        assertThat(list("Dan", "Matt").index(0), is("Dan"));
        assertThat(list("Dan", "Matt").index(1), is("Matt"));
    }

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
    public void supportsFilter() throws Exception {
        assertThat(list(1, 2, 3, 1, 2, 3).filter(Predicates.is(3).not()), hasExactly(1, 2, 1, 2));
        assertThat(list(1, 2, 3, 1, 2, 3).filter(Predicates.is(3)), hasExactly(3, 3));
    }

    @Test
    @Ignore
    public void supportsFilterIsPrettyFast() throws Exception {
        final ImmutableList<Number> range = range(1, 1000).toImmutableList();
        TimeReport report = TimeReport.time(100000, new Function<ImmutableList<Number>>() {
            @Override
            public ImmutableList<Number> call() throws Exception {
                return range.filter(Predicates.<Number>is(3));
            }
        });
        System.out.println(report);
    }

    @Test
    public void supportsRemove() throws Exception {
        assertThat(list(1, 2, 3, 4, 5, 6).remove(3), hasExactly(1, 2, 4, 5, 6));
        assertThat(list(1, 2, 3, 4, 5, 6).remove(6), hasExactly(1, 2, 3, 4, 5));
    }

    @Test
    @Ignore
    public void removeIsPrettyFast() throws Exception {
        final ImmutableList<Number> range = range(1, 1000).toImmutableList();
        TimeReport report = TimeReport.time(1000000, new Function<ImmutableList<Number>>() {
            @Override
            public ImmutableList<Number> call() throws Exception {
                return range.remove(3);
            }
        });
        System.out.println(report);
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

    @Test
    public void canJoin() throws Exception {
        ImmutableList<Integer> join = list(1, 2, 3, 4).joinTo(list(4, 3, 2, 1));
        assertThat(join, hasExactly(1, 2, 3, 4, 4, 3, 2, 1));
        ImmutableSortedSet<Integer> sortedSet = list(2, 1, 4, 3).joinTo(sortedSet(3, 4));
        assertThat(sortedSet, hasExactly(1, 2, 3, 4));
    }
}
