package com.googlecode.totallylazy.callables;

import org.junit.Test;

import static com.googlecode.totallylazy.callables.CountingCallable.counting;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TimeCallableTest {
    @Test
    public void canTimeACall() throws Exception {
        TimeReport report = new TimeReport();
        counting().sleep(10).lazy().
                time(report).
                repeat().take(100).realise();
        System.out.println(report);
        assertThat(report.maximum(), is(greaterThan(10.0)));
        assertThat(report.average(), is(lessThan(1.0)));
    }
}
