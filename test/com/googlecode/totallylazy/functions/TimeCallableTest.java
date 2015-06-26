package com.googlecode.totallylazy.functions;

import org.junit.Test;

import static com.googlecode.totallylazy.functions.CountingCallable.counting;
import static com.googlecode.totallylazy.matchers.NumberMatcher.greaterThanOrEqualTo;
import static com.googlecode.totallylazy.matchers.NumberMatcher.lessThan;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TimeCallableTest {
    @Test
    public void canTimeACall() throws Exception {
        TimeReport report = new TimeReport();
        CountingCallable.counting().sleep(10).lazy().
                time(report).
                repeat().take(100).realise();
        System.out.println(report);
        assertThat(report.maximum(), is(greaterThanOrEqualTo(10)));
        assertThat(report.average(), is(lessThan(1)));
    }
}
