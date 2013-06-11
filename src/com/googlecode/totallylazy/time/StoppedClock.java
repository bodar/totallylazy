package com.googlecode.totallylazy.time;

import java.util.Date;

import static com.googlecode.totallylazy.time.Dates.date;

public class StoppedClock implements Clock {
    private final Date date;

    public StoppedClock(Date date) {
        this.date = date(date);
    }

    public static StoppedClock stoppedClock(Date date) {
        return new StoppedClock(date);
    }

    public static StoppedClock stoppedClock() {
        return stoppedClock(new Date());
    }

    public Date now() {
        return date(date);
    }
}