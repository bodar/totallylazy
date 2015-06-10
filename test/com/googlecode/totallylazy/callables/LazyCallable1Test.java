package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Lazy;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.matchers.Matchers;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.googlecode.totallylazy.Callers.callConcurrently;
import static com.googlecode.totallylazy.callables.CountingCallable1.counting;
import static com.googlecode.totallylazy.callables.LazyCallable1.lazy;
import static com.googlecode.totallylazy.matchers.NumberMatcher.is;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;

public class LazyCallable1Test {
    @Test
    public void remembersException() throws Exception {
        final AtomicInteger count = new AtomicInteger();

        LazyCallable1<String, String> lazy = LazyCallable1.lazy(new Callable1<String, String>() {
            @Override
            public String call(String s) throws Exception {
                int i = count.incrementAndGet();
                throw new RuntimeException(format("Called %d times.", i));
            }
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
        CountingCallable1<Number, Number> counting = counting(increment);
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
        CountingCallable1<Number, Number> counting = counting(increment);
        Function1<Number, Number> lazyCallable = lazy(counting);

        assertThat(lazyCallable.call(0), is(1));
        assertThat(lazyCallable.call(0), is(1));
        assertThat(counting.count(0), is(1));
    }
}
