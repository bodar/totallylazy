package com.googlecode.totallylazy.time;

import com.googlecode.totallylazy.matchers.NumberMatcher;
import org.junit.Test;

import java.util.Date;

import static com.googlecode.totallylazy.Assert.assertThat;


public class MinutesTest {
    @Test
    public void betweenReturnsWholeMinutes() throws Exception {
        Date start = Dates.date(1983, 10, 7, 13, 30, 15);
        assertThat(Minutes.between(start, Seconds.add(start, 40)), NumberMatcher.is(0));
        assertThat(Minutes.between(start, Seconds.add(start, 60)), NumberMatcher.is(1));
        assertThat(Minutes.between(start, Seconds.add(start, 70)), NumberMatcher.is(1));
    }
}
