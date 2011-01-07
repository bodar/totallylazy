package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.matchers.NumberMatcher;
import org.junit.Test;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callers.callConcurrently;
import static com.googlecode.totallylazy.callables.CountingCallable.counting;
import static com.googlecode.totallylazy.callables.LazyCallable.lazy;
import static com.googlecode.totallylazy.callables.SleepyCallable.sleepy;
import static com.googlecode.totallylazy.callables.TimeCallable.time;
import static com.googlecode.totallylazy.matchers.NumberMatcher.between;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
public class LazyCallableTest {
    @Test
    public void instancesDoNotInteract() throws Exception {
        CountingCallable<Integer> firstCount = counting();
        TimeReport firstTimes = new TimeReport();
        Callable<Integer> firstLazy = time(lazy(sleepy(firstCount, 10)), firstTimes);

        CountingCallable<Integer> secondCount = counting();
        TimeReport secondTimes = new TimeReport();
        Callable<Integer> secondLazy = time(lazy(sleepy(secondCount, 20)), secondTimes);

        callConcurrently(firstLazy, secondLazy).realise();

        assertThat(firstCount.count(), is(1));
        assertThat(secondCount.count(), is(1));
        assertThat(firstTimes.lastTime(), is(between(10, 12)));
        assertThat(secondTimes.lastTime(), is(between(20, 22)));
    }

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
