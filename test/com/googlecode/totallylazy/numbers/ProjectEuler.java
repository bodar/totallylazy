package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.predicates.WherePredicate;
import org.junit.Test;

import static com.googlecode.totallylazy.Callables.asString;
import static com.googlecode.totallylazy.Predicates.or;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.isPalindrome;
import static com.googlecode.totallylazy.matchers.NumberMatcher.is;
import static com.googlecode.totallylazy.numbers.Numbers.even;
import static com.googlecode.totallylazy.numbers.Numbers.fibonacci;
import static com.googlecode.totallylazy.numbers.Numbers.isZero;
import static com.googlecode.totallylazy.numbers.Numbers.lcm;
import static com.googlecode.totallylazy.numbers.Numbers.lessThanOrEqualTo;
import static com.googlecode.totallylazy.numbers.Numbers.maximum;
import static com.googlecode.totallylazy.numbers.Numbers.mod;
import static com.googlecode.totallylazy.numbers.Numbers.multiply;
import static com.googlecode.totallylazy.numbers.Numbers.primeFactors;
import static com.googlecode.totallylazy.numbers.Numbers.primes;
import static com.googlecode.totallylazy.numbers.Numbers.range;
import static com.googlecode.totallylazy.numbers.Numbers.squared;
import static com.googlecode.totallylazy.numbers.Numbers.subtract;
import static com.googlecode.totallylazy.numbers.Numbers.sum;
import static com.googlecode.totallylazy.predicates.WherePredicate.asWhere;
import static com.googlecode.totallylazy.predicates.WherePredicate.where;
import static org.hamcrest.MatcherAssert.assertThat;

public class ProjectEuler {
    @Test
    public void problem1() throws Exception {
        assertThat(range(1, 999).filter(or(sequence(3, 5).map(asWhere(mod(), isZero())))).reduce(sum()), is(233168));
    }

    @Test
    public void problem2() throws Exception {
        assertThat(fibonacci().takeWhile(lessThanOrEqualTo(4000000)).filter(even()).reduce(sum()), is(4613732));
    }

    @Test
    public void problem3() throws Exception {
        assertThat(primeFactors(600851475143L).last(), is(6857));
    }

    @Test
    public void problem4() throws Exception {
        assertThat(range(999, 100).cartesianProduct().map(multiply().pair()).
                filter(where(asString(), isPalindrome())).reduce(maximum()), is(906609));
    }

    @Test
    public void problem5() throws Exception {
        assertThat(range(1, 20).reduce(lcm()), is(232792560));
    }

    @Test
    public void problem6() throws Exception {
        assertThat(subtract(squared(range(1, 100).reduce(sum())), range(1, 100).map(squared()).reduce(sum())), is(25164150));
    }

    @Test
    public void problem7() throws Exception {
        assertThat(primes().drop(10000).head(), is(104743));
    }
}
