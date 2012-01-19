package com.googlecode.totallylazy;

import com.googlecode.totallylazy.matchers.NumberMatcher;
import org.junit.Test;

import static com.googlecode.totallylazy.Callables.compose;
import static com.googlecode.totallylazy.Callables.curry;
import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Callables.unCurry;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.matchers.NumberMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.NumberMatcher.is;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static com.googlecode.totallylazy.numbers.Numbers.multiply;
import static com.googlecode.totallylazy.numbers.Numbers.range;
import static org.hamcrest.MatcherAssert.assertThat;

public class FunctionsTest {
    @Test
    public void canComposeFunctions() throws Exception {
        assertThat(compose(add(10), multiply(3)).apply(2), NumberMatcher.is(36));
        assertThat(add(10).map(multiply(3)).apply(2), NumberMatcher.is(36));
        assertThat(add(10).then(multiply(3)).apply(2), NumberMatcher.is(36));
        assertThat(add(10).curry(2).map(multiply(3)).apply(), NumberMatcher.is(36));
        assertThat(add(10).curry(2).then(multiply(3)).apply(), NumberMatcher.is(36));
    }

    @Test
    public void canCallWithAValue() throws Exception {
        Sequence<Number> computations = range(1, 5).map(multiply()).map(Callables.<Number, Number>callWith(2));
        assertThat(computations, hasExactly(2, 4, 6, 8, 10));
    }
}
