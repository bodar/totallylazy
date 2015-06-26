package com.googlecode.totallylazy.functions;

import com.googlecode.totallylazy.Sequence;
import org.junit.Test;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.functions.Callables.returns;
import static com.googlecode.totallylazy.Callers.callConcurrently;
import static com.googlecode.totallylazy.functions.CountingCallable.counting;
import static com.googlecode.totallylazy.functions.LazyCallable.lazy;
import static com.googlecode.totallylazy.functions.TimeReport.time;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class LazyCallableTest {
    @Test
    public void isVeryFast() throws Exception {
        System.out.println("base = " + TimeReport.time(1000, returns(1)));
        System.out.println("lazy = " + TimeReport.time(1000, LazyCallable.lazy(returns(1))));
    }

    @Test
    public void isThreadSafe() throws Exception {
        CountingCallable<Integer> callable = CountingCallable.counting();
        Function0<Integer> lazyCallable = callable.sleep(10).lazy();

        Sequence<Integer> result = callConcurrently(lazyCallable, lazyCallable).realise();

        assertThat(callable.count(), is(1));
        assertThat(result.first(), is(0));
        assertThat(result.second(), is(0));
    }

    @Test
    public void onlyCallsUnderlyingCallableOnce() throws Exception {
        CountingCallable<Integer> callable = CountingCallable.counting();
        Callable<Integer> lazyCallable = callable.lazy();

        assertThat(lazyCallable.call(), is(0));
        assertThat(lazyCallable.call(), is(0));
        assertThat(callable.count(), is(1));
    }
}
