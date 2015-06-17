package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.matchers.Matchers;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.googlecode.totallylazy.Callers.callConcurrently;
import static com.googlecode.totallylazy.callables.CountingFunction.counting;
import static com.googlecode.totallylazy.callables.LazyFunction.lazy;
import static com.googlecode.totallylazy.matchers.NumberMatcher.is;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;

public class LazyFunction1Test {
    @Test
    public void remembersException() throws Exception {
        final AtomicInteger count = new AtomicInteger();

        LazyFunction<String, String> lazy = LazyFunction.lazy(s -> {
            int i = count.incrementAndGet();
            throw new RuntimeException(format("Called %d times.", i));
        });

        try {
            lazy.call(null);
        } catch (Exception e){
            assertThat(e.getMessage(), Matchers.is("Called 1 times."));
        }
        try {
            lazy.call(null);
        } catch (Exception e){
            assertThat(e.getMessage(), Matchers.is("Called 1 times."));
        }

    }
    @Test
    public void isThreadSafe() throws Exception {
        CountingFunction<Number, Number> counting = counting(increment);
        Function1<Number, Number> lazyCallable1 = counting.sleep(10).lazy();

        Sequence<Number> result = callConcurrently(
                lazyCallable1.deferApply(3), lazyCallable1.deferApply(6),
                lazyCallable1.deferApply(3), lazyCallable1.deferApply(6)).realise();

        assertThat(counting.count(3), is(1));
        assertThat(counting.count(6), is(1));
        assertThat(result.first(), is(4));
        assertThat(result.second(), is(7));
    }

    @Test
    public void onlyCallsUnderlyingCallableOnce() throws Exception {
        CountingFunction<Number, Number> counting = counting(increment);
        Function1<Number, Number> lazyCallable = lazy(counting);

        assertThat(lazyCallable.call(0), is(1));
        assertThat(lazyCallable.call(0), is(1));
        assertThat(counting.count(0), is(1));
    }
}
