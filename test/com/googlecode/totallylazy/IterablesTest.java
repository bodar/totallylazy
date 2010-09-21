package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Callables.increment;
import static com.googlecode.totallylazy.Iterables.iterate;
import static com.googlecode.totallylazy.Iterables.range;
import static com.googlecode.totallylazy.Predicates.even;
import static com.googlecode.totallylazy.Predicates.odd;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class IterablesTest {
    @Test
    public void supportsRange() throws Exception {
        assertThat(range(5), hasItems(0, 1, 2, 3, 4));
        assertThat(range(0, 5), hasItems(0, 1, 2, 3, 4));
        assertThat(range(0, 5, 2), hasItems(0, 2, 4));
    }

    @Test
    public void supportsIterate() throws Exception {
        LazyIterable<Integer> numbers = iterate(increment(), 1);
        assertThat(numbers, hasItems(1, 2, 3, 4, 5));
    }

    @Test
    public void canCombineIterateWithOtherOperations() throws Exception {
        final LazyIterable<Integer> numbers = iterate(increment(), 1);
        assertThat(numbers.filter(even()), hasItems(2, 4, 6));
        assertThat(numbers.filter(odd()), hasItems(1, 3, 5, 7, 9));
    }


}
