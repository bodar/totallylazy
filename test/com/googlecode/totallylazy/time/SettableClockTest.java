package com.googlecode.totallylazy.time;

import org.junit.Test;

import java.util.Date;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.time.Dates.date;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;

public class SettableClockTest {
    @Test
    public void changingDateIntoSettableClockDoesNotChangeStoppedClock() throws Exception {
        Date dateIn = date(2000, 1, 1);
        SettableClock clock = new SettableClock(dateIn);
        dateIn.setTime(date(1974, 10, 29).getTime());
        assertThat(clock.now(), is(date(2000, 1, 1)));

        clock.now(date(1974, 11, 13));
        assertThat(clock.now(), is(date(1974, 11, 13)));
    }

    @Test
    public void changingDateOutOfSettableClockDoesNotChangeStoppedClock() throws Exception {
        SettableClock clock = new SettableClock(date(2000, 1, 1));
        clock.now().setTime(date(1974, 10, 29).getTime());
        assertThat(clock.now(), is(date(2000, 1, 1)));
    }
}
