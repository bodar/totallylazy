package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Callables.add;
import static com.googlecode.totallylazy.Callables.increment;
import static com.googlecode.totallylazy.HasExactlyMatcher.hasExactly;
import static com.googlecode.totallylazy.Predicates.even;
import static com.googlecode.totallylazy.Predicates.odd;
import static com.googlecode.totallylazy.Sequences.*;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class SequencesTest {
    @Test
    public void supportsPrimes() throws Exception {
        Sequence<Integer> primes = primes();
        assertThat(primes, hasItems(2,3,5,7,11,13,17,19,23,29));
    }

    @Test
    public void supportsFibonacci() throws Exception {
        assertThat(fibonacci(), hasItems(0, 1, 1, 2, 3, 5));
    }

    @Test
    public void supportsCharacters() throws Exception {
        assertThat(characters("text"), hasExactly('t', 'e', 'x', 't'));
        assertThat(characters("text").drop(2).toString(""), is("xt"));
    }

    @Test
    public void supportsRange() throws Exception {
        assertThat(range(5), hasExactly(0, 1, 2, 3, 4));
        assertThat(range(0, 5), hasExactly(0, 1, 2, 3, 4));
        assertThat(range(0, 5, 2), hasExactly(0, 2, 4));
    }

    @Test
    public void supportsIterate() throws Exception {
        Sequence<Integer> numbers = iterate(increment(), 1);
        assertThat(numbers, hasItems(1, 2, 3, 4, 5));
    }

    @Test
    public void canCombineIterateWithOtherOperations() throws Exception {
        final Sequence<Integer> numbers = iterate(increment(), 1);
        assertThat(numbers.filter(even()), hasItems(2, 4, 6));
        assertThat(numbers.filter(odd()), hasItems(1, 3, 5, 7, 9));
    }
}
