package com.googlecode.totallylazy.time;

import com.googlecode.totallylazy.matchers.NumberMatcher;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertThat;

public class HoursTest {

    @Test
    public void betweenReturnsWholeHours() throws Exception {
        Date start = Dates.date(1983, 10, 7, 13, 30);
        assertThat(Hours.between(start, Minutes.add(start, 40)), NumberMatcher.is(0));
        assertThat(Hours.between(start, Minutes.add(start, 60)), NumberMatcher.is(1));
        assertThat(Hours.between(start, Minutes.add(start, 70)), NumberMatcher.is(1));
    }
}
