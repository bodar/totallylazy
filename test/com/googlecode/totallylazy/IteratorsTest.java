package com.googlecode.totallylazy;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static com.googlecode.totallylazy.Arrays.list;
import static com.googlecode.totallylazy.Sequences.sequence;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class IteratorsTest {
    @Test
    public void supportsEquality() throws Exception {
        assertThat(Iterators.equalsTo(list(1, 2, 3).iterator(), list(1, 2, 3).iterator()), is(true));
        assertThat(Iterators.equalsTo(list(1, 2, 3).iterator(), list(3, 2, 1).iterator()), is(false));
        assertThat(Iterators.equalsTo(list(1, 2, 3).iterator(), list("1", "2", "3").iterator()), is(false));
        assertThat(Iterators.equalsTo(list(1, 2).iterator(), list(1, 2, 3).iterator()), is(false));
        assertThat(Iterators.equalsTo(list(1, 2, 3).iterator(), list(1, 2).iterator()), is(false));
        assertThat(Iterators.equalsTo(sequence(1, 2, 3).iterator(), list(1, 2, 3).iterator()), is(true));
        assertThat(Iterators.equalsTo(sequence(Dates.date(2000, 1, 1), 1).iterator(), sequence(Dates.date(2000, 1, 1), 1).iterator()), is(true));
        assertThat(Iterators.equalsTo(sequence(Dates.date(2000, 1, 1), 1).iterator(), sequence(null, 1).iterator()), is(false));
        assertThat(Iterators.equalsTo(sequence(null, 1).iterator(), sequence(Dates.date(2000, 1, 1), 1).iterator()), is(false));
        assertThat(Iterators.equalsTo(sequence(1, null, 3).iterator(), sequence(1, 2, 3).iterator()), is(false));
        assertThat(Iterators.equalsTo(sequence(1, 2, 3).iterator(), sequence(1, null, 3).iterator()), is(false));
        assertThat(Iterators.equalsTo(sequence(1, null, 3).iterator(), sequence(1, null, 3).iterator()), is(true));

    }
}
