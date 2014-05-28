package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.numbers.Numbers;
import org.junit.Test;

import static com.googlecode.totallylazy.callables.CountingCallable.counting;
import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;

public class TimeCallableTest {
    @Test
    public void canTimeACall() throws Exception {
        TimeReport report = new TimeReport();
        counting().sleep(10).lazy().
                time(report).
                repeat().take(100).realise();
        System.out.println(report);
        assertThat(report.maximum(), is(Numbers.greaterThanOrEqualTo(10)));
        assertThat(report.average(), is(Numbers.lessThan(1)));
    }
}
