package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.callables.TimeReport;
import org.hamcrest.Matchers;
import org.junit.Test;

import static com.googlecode.totallylazy.Callables.curry;
import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.callables.TimeCallable.time;
import static com.googlecode.totallylazy.numbers.BigIntegerOperators.bigInteger;
import static com.googlecode.totallylazy.numbers.Numbers.*;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.IterableMatcher.startsWith;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

public class NumbersTest {
    @Test
    public void supportsSorting() throws Exception {
        final Sequence<Number> unsorted = numbers(5d, 1, 4L, bigInteger(2), 3f);
        final Sequence<Number> sorted = unsorted.sortBy(descending());
        assertThat(sorted, hasExactly(5d, 4L, 3f, bigInteger(2), 1));
    }

    @Test
    public void shouldBePrettyFast() throws Exception {
        TimeReport report = new TimeReport();
        repeat(time(sum(), iterate(increment(), 0).take(10000), report)).take(100).realise();
        System.out.println(report);
        assertThat(report.average(), is(lessThan(10.0)));
    }

    @Test
    public void toStringingAnInfiniteListWillTruncateByDefault() throws Exception {
        assertThat(primes().toString(), is(primes().take(100).toString()));
    }

//    @Test
//    public void supportsPrimeFactorsOfLargeNumbers() throws Exception {
//        time(primeFactorsOf(600851475143L));
//        assertThat(time(primeFactorsOf(600851475143L)), hasExactly(71, 839, 1471, 6857));
//    }

    @Test
    public void supportsPrimeFactors() throws Exception {
        assertThat(primeFactorsOf(13195), hasExactly(5, 7, 13, 29));
    }

    @Test
    public void supportsPrimeFactorsOfSmallNumbers() throws Exception {
        assertThat(primeFactorsOf(300), hasExactly(2, 3, 5));
    }

    @Test
    public void supportsPrimes() throws Exception {
        assertThat(primes(), startsWith(2, 3, 5, 7, 11, 13, 17, 19, 23, 29));
    }

    @Test
    public void primesIsPrettyFastAndIsMemorised() throws Exception {
        TimeReport report = new TimeReport();
        repeat(time(primes().take(1000), report)).take(100).realise();
        System.out.println(report);
        assertThat(report.average(), Matchers.is(lessThan(10.0)));
    }



    @Test
    public void supportsFibonacci() throws Exception {
        assertThat(fibonacci(), startsWith(0, 1, 1, 2, 3, 5));
    }

    @Test
    public void supportsPowersOf() throws Exception {
        assertThat(powersOf(2), startsWith(1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048));
        assertThat(powersOf(3), startsWith(1, 3, 9, 27, 81, 243, 729, 2187, 6561, 19683, 59049));
        assertThat(powersOf(10), startsWith(1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000, 10000000000L, 100000000000L));
    }
}
