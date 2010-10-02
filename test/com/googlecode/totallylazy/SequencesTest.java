package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Callables.increment;
import static com.googlecode.totallylazy.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.IterableMatcher.startsWith;
import static com.googlecode.totallylazy.Predicates.even;
import static com.googlecode.totallylazy.Predicates.odd;
import static com.googlecode.totallylazy.Sequences.*;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SequencesTest {
    @Test
    public void toStringingAnInfiniteListWillTruncateByDefault() throws Exception {
        Sequence<Integer> primes = primes();
        assertThat(primes.toString(), is(primes.take(100).toString()));
    }

    @Test
    public void supportsPrimeFactors() throws Exception {
        Sequence<Integer> primeFactors = primeFactorsOf(13195);
        assertThat(primeFactors, hasExactly(5,7,13,29));
    }

    @Test
    public void supportsPrimes() throws Exception {
        Sequence<Integer> primes = primes();
        assertThat(primes, startsWith(2,3,5,7,11,13,17,19,23,29));
    }

    @Test
    public void supportsFibonacci() throws Exception {
        assertThat(fibonacci(), startsWith(0, 1, 1, 2, 3, 5));
    }

    @Test
    public void supportsCharacters() throws Exception {
        assertThat(characters("text"), hasExactly('t', 'e', 'x', 't'));
        assertThat(characters("text".toCharArray()), hasExactly('t', 'e', 'x', 't'));
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
        assertThat(numbers, startsWith(1, 2, 3, 4, 5));
    }

    @Test
    public void canCombineIterateWithOtherOperations() throws Exception {
        final Sequence<Integer> numbers = iterate(increment(), 1);
        assertThat(numbers.filter(even()), startsWith(2, 4, 6));
        assertThat(numbers.filter(odd()), startsWith(1, 3, 5, 7, 9));
    }
}
