package com.googlecode.totallylazy.callables;

import org.junit.Test;

import static com.googlecode.totallylazy.callables.CountingCallable.counting;
import static com.googlecode.totallylazy.callables.LazyCallable.lazy;
import static com.googlecode.totallylazy.callables.SleepyCallable.sleepy;
import static com.googlecode.totallylazy.callables.TimeCallable.time;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

public class TimeCallableTest {
    @Test
    public void canTimeACall() throws Exception {
        TimeReporter reporter = new TimeReporter();
        TimeCallable<Integer> timeCallable = time(lazy(sleepy(counting(), 10)), reporter);
        timeCallable.call();
        assertThat(reporter.time(), is(greaterThan(10.0)));

        timeCallable.call();
        assertThat(reporter.time(), is(lessThan(1.0)));
    }
}