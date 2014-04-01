package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Sequence;
import org.junit.Test;

import static com.googlecode.totallylazy.Callers.callConcurrently;
import static com.googlecode.totallylazy.callables.CountingFunction.counting;
import static com.googlecode.totallylazy.callables.LazyFunction.lazy;
import static com.googlecode.totallylazy.matchers.NumberMatcher.is;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static org.hamcrest.MatcherAssert.assertThat;

public class LazyFunctionTest {
    @Test
    public void isThreadSafe() throws Exception {
        CountingFunction<Number, Number> counting = counting(increment);
        Function<Number, Number> lazyFunction = counting.sleep(10).lazy();

        Sequence<Number> result = callConcurrently(
                lazyFunction.deferApply(3), lazyFunction.deferApply(6),
                lazyFunction.deferApply(3), lazyFunction.deferApply(6)).realise();

        assertThat(counting.count(3), is(1));
        assertThat(counting.count(6), is(1));
        assertThat(result.first(), is(4));
        assertThat(result.second(), is(7));
    }

    @Test
    public void onlyCallsUnderlyingCallableOnce() throws Exception {
        CountingFunction<Number, Number> counting = counting(increment);
        Function<Number, Number> lazyCallable = lazy(counting);

        assertThat(lazyCallable.call(0), is(1));
        assertThat(lazyCallable.call(0), is(1));
        assertThat(counting.count(0), is(1));
    }
}
