package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.matchers.NumberMatcher;
import org.junit.Test;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callables.curry;
import static com.googlecode.totallylazy.Callers.callConcurrently;
import static com.googlecode.totallylazy.callables.CountingCallable1.counting;
import static com.googlecode.totallylazy.callables.LazyCallable1.lazy;
import static com.googlecode.totallylazy.callables.SleepyCallable1.sleepy;
import static com.googlecode.totallylazy.matchers.NumberMatcher.is;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static org.hamcrest.MatcherAssert.assertThat;

public class LazyCallable1Test {
    @Test
    public void isThreadSafe() throws Exception {
        CountingCallable1<Number, Number> counting = counting(increment());
        Callable1<Number, Number> lazyCallable1 = lazy(sleepy(counting, 10));

        Sequence<Number> result = callConcurrently(
                curry(lazyCallable1, 3), curry(lazyCallable1, 6),
                curry(lazyCallable1, 3), curry(lazyCallable1, 6)).realise();

        assertThat(counting.count(3), is(1));
        assertThat(counting.count(6), is(1));
        assertThat(result.first(), is(4));
        assertThat(result.second(), is(7));
    }

    @Test
    public void onlyCallsUnderlyingCallableOnce() throws Exception {
        CountingCallable1<Number, Number> counting = counting(increment());
        Callable1<Number, Number> lazyCallable = lazy(counting);

        assertThat(lazyCallable.call(0), is(1));
        assertThat(lazyCallable.call(0), is(1));
        assertThat(counting.count(0), is(1));
    }
}
