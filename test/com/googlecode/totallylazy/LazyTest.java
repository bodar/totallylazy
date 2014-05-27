package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.CountingCallable;
import org.hamcrest.core.Is;
import org.junit.Test;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Callers.callConcurrently;
import static com.googlecode.totallylazy.Lazy.lazy;
import static com.googlecode.totallylazy.callables.CountingCallable.counting;
import static com.googlecode.totallylazy.callables.TimeReport.time;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LazyTest {
    @Test
    public void canCreateALazyValue() throws Exception {
        final int[] count = {0};

        Lazy<String> lazy = Lazy.value(() -> "Hello " + ++count[0]);

        assertThat(lazy.value(), is("Hello 1"));
        assertThat(lazy.value(), is("Hello 1"));
    }

    @Test
    public void isVeryFast() throws Exception {
        System.out.println("base = " + time(1000, returns(1)));
        System.out.println("lazy = " + time(1000, lazy(returns(1))));
    }

    @Test
    public void isThreadSafe() throws Exception {
        CountingCallable<Integer> callable = counting();
        Returns<Integer> lazyCallable = callable.sleep(10).lazy();

        Sequence<Integer> result = callConcurrently(lazyCallable, lazyCallable).realise();

        assertThat(callable.count(), Is.is(1));
        assertThat(result.first(), Is.is(0));
        assertThat(result.second(), Is.is(0));
    }

    @Test
    public void onlyCallsUnderlyingCallableOnce() throws Exception {
        CountingCallable<Integer> callable = counting();
        Callable<Integer> lazyCallable = callable.lazy();

        assertThat(lazyCallable.call(), Is.is(0));
        assertThat(lazyCallable.call(), Is.is(0));
        assertThat(callable.count(), Is.is(1));
    }

}
