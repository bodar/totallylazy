package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.TimeCallable;
import com.googlecode.totallylazy.callables.TimeReporter;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callables.increment;
import static com.googlecode.totallylazy.Predicates.even;
import static com.googlecode.totallylazy.Predicates.odd;
import static com.googlecode.totallylazy.Sequences.*;
import static com.googlecode.totallylazy.callables.TimeCallable.time;
import static com.googlecode.totallylazy.predicates.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.predicates.IterableMatcher.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;

public class SequencesTest {
    @Test
    public void toStringingAnInfiniteListWillTruncateByDefault() throws Exception {
        assertThat(primes().toString(), is(primes().take(100).toString()));
    }

    @Test
    public void supportsPrimeFactors() throws Exception {
        assertThat(primeFactorsOf(13195), hasExactly(5, 7, 13, 29));
    }

    @Test
    public void supportsPrimes() throws Exception {
        assertThat(primes(), startsWith(2, 3, 5, 7, 11, 13, 17, 19, 23, 29));
    }

    @Test
    public void primesIsPrettyFastAndIsMemorised() throws Exception {
        TimeReporter reporter = new TimeReporter();
        TimeCallable<Sequence<Integer>> timeCallable = time(new Callable<Sequence<Integer>>() {
            public Sequence<Integer> call() throws Exception {
                return primes().take(1000).realise();
            }
        }, reporter);
        timeCallable.call();
        assertThat(reporter.time(), Matchers.is(lessThan(200.0)));

        timeCallable.call();
        assertThat(reporter.time(), Matchers.is(lessThan(10.0)));
    }


    @Test
    public void supportsFibonacci() throws Exception {
        assertThat(fibonacci(), startsWith(0, 1, 1, 2, 3, 5));
    }

    @Test
    public void supportsPowersOf() throws Exception {
        assertThat(powersOf(2), startsWith(1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048));
        assertThat(powersOf(3), startsWith(1, 3, 9, 27, 81, 243, 729, 2187, 6561, 19683, 59049));
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
        assertThat(iterate(increment(), 1), startsWith(1, 2, 3, 4, 5));
    }

    @Test
    public void canCombineIterateWithOtherOperations() throws Exception {
        final Sequence<Integer> numbers = iterate(increment(), 1);
        assertThat(numbers.filter(even()), startsWith(2, 4, 6));
        assertThat(numbers.filter(odd()), startsWith(1, 3, 5, 7, 9));
    }
}
