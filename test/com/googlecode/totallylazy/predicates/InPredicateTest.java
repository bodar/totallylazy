package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Sets;
import org.junit.Test;

import java.util.Set;

import static com.googlecode.totallylazy.Predicates.never;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.predicates.InPredicate.in;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;
import static com.googlecode.totallylazy.Predicates.instanceOf;

public class InPredicateTest {
    @Test
    public void collapsesEmptyInClauseToFalse() throws Exception {
        Set<Integer> empty = Sets.set();
        LogicalPredicate<Integer> predicate = in(empty);
        assertThat(empty.contains(1), is(predicate.matches(1)));
        assertThat(predicate, instanceOf(never().getClass()));
    }

    @Test
    public void supportsToString() throws Exception {
        assertThat(in(1, 2, 3).toString(), is("in('1','2','3')"));
    }

    @Test
    public void supportsEquality() throws Exception {
        assertThat(in(1, 2, 3).equals(in(1, 2, 3)), is(true));
        assertThat(in(1, 2, 3).equals(in('1', '2', '3')), is(false));
    }

    @Test
    public void supportsHashCode() throws Exception {
        assertThat(in(1, 2, 3).hashCode(), is(in(1, 2, 3).hashCode()));
        assertThat(in(1, 2, 3).hashCode(), not(in('1', '2', '3').hashCode()));
    }
}
