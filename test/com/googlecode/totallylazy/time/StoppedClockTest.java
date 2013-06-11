package com.googlecode.totallylazy.time;

import org.junit.Test;

import java.util.Date;

import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.time.Dates.date;
import static org.hamcrest.MatcherAssert.assertThat;

public class StoppedClockTest {
    @Test
    public void changingDateIntoStoppedClockDoesNotChangeStoppedClock() throws Exception {
        Date dateIn = date(2000, 1, 1);
        StoppedClock clock = StoppedClock.stoppedClock(dateIn);
        dateIn.setTime(date(1974, 10, 29).getTime());
        assertThat(clock.now(), is(date(2000, 1, 1)));
    }

    @Test
    public void changingDateOutOfStoppedClockDoesNotChangeStoppedClock() throws Exception {
        StoppedClock clock = StoppedClock.stoppedClock(date(2000, 1, 1));
        clock.now().setTime(date(1974, 10, 29).getTime());
        assertThat(clock.now(), is(date(2000, 1, 1)));
    }
}