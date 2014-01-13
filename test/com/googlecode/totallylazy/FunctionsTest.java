package com.googlecode.totallylazy;

import com.googlecode.totallylazy.matchers.Matchers;
import com.googlecode.totallylazy.numbers.Numbers;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static com.googlecode.totallylazy.Callables.compose;
import static com.googlecode.totallylazy.Callables.uncurry3;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.matchers.NumberMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.NumberMatcher.is;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static com.googlecode.totallylazy.numbers.Numbers.multiply;
import static com.googlecode.totallylazy.numbers.Numbers.numbers;
import static com.googlecode.totallylazy.numbers.Numbers.range;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class FunctionsTest {
    @Test
    public void supportsApplicativeUsageWithOption() throws Exception {
        assertThat(add(3).$(some(3)), Matchers.is(some((Number)6)));
    }

    @Test
    public void supportsApplicativeUsageWithEither() throws Exception {
        assertThat(add(3).$(Either.<String, Number>right(3)), CoreMatchers.is(Either.<String, Number>right(6)));
    }

    @Test
    public void supportsApplicativeUsageWithSequence() throws Exception {
        assertThat(add(3).$(numbers(9, 1)), Matchers.is(numbers(12, 4)));
    }

    @Test
    public void canComposeCurriedFunctions() throws Exception {
        assertThat(add().then(multiply()).apply(2).apply(10).apply(3), is(36));
        assertThat(add().then(multiply().then(add().then(multiply()))).apply(2).apply(10).apply(3).apply(2).apply(10), is(380));
    }

    @Test
    public void canComposeFunctions() throws Exception {
        assertThat(compose(add(10), multiply(3)).apply(2), is(36));
        assertThat(add(10).map(multiply(3)).apply(2), is(36));
        assertThat(add(10).then(multiply(3)).apply(2), is(36));
        assertThat(add(10).deferApply(2).map(multiply(3)).apply(), is(36));
        assertThat(add(10).deferApply(2).then(multiply(3)).apply(), is(36));
    }

    @Test
    public void canCallWithAValue() throws Exception {
        Sequence<Number> computations = range(1, 5).map(multiply().curry()).map(f -> f.call(2));
        assertThat(computations, hasExactly(2, 4, 6, 8, 10));
    }

    @Test
    public void canPartiallyApplyAFunction3() throws Exception {
        assertThat(addThenMultiple().apply(10).apply(2).apply(3), is(36));
        assertThat(addThenMultiple().apply(10, 2).apply(3), is(36));
        assertThat(addThenMultiple().apply(10, 2, 3), is(36));
    }

    @Test
    public void canCaptureArgument() throws Exception {
        assertThat(multiply(10).capturing().apply(1), equalTo(Pair.<Number,Number>pair(1, 10)));
    }

    private Function3<Number, Number, Number, Number> addThenMultiple() {
        return new Function3<Number, Number, Number, Number>() {
            @Override
            public Number call(Number a, Number b, Number c) throws Exception {
                return Numbers.multiply(Numbers.add(a,b), c);
            }
        };
    }
}
