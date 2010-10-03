package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Sequence;
import org.junit.Test;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callers.callConcurrently;
import static com.googlecode.totallylazy.callables.CountingCallable.counting;
import static com.googlecode.totallylazy.callables.LazyCallable.lazy;
import static com.googlecode.totallylazy.callables.SleepyCallable.sleepy;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LazyCallableTest {
    @Test
    public void isThreadSafe() throws Exception {
        CountingCallable callable = counting();
        Callable<Integer> lazyCallable = lazy(sleepy(callable, 10));

        Sequence<Integer> result = callConcurrently(lazyCallable, lazyCallable);

        assertThat(callable.count(), is(1));
        assertThat(result.first(), is(0));
        assertThat(result.second(), is(0));
    }

    @Test
    public void onlyCallsUnderlyingCallableOnce() throws Exception {
        CountingCallable callable = counting();
        Callable<Integer> lazyCallable = lazy(callable);

        assertThat(lazyCallable.call(), is(0));
        assertThat(lazyCallable.call(), is(0));
        assertThat(callable.count(), is(1));
    }
}
