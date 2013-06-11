package com.googlecode.totallylazy.time;

import java.util.Date;

import static com.googlecode.totallylazy.time.Dates.date;

public class SettableClock implements Clock {
    private volatile Date date;

    public SettableClock() {
        this(Dates.date(2001, 1, 1));
    }

    public SettableClock(Date date) {
        this.date = date(date);
    }

    public static SettableClock settableClock() {
        return new SettableClock();
    }

    public static SettableClock settableClock(Date date) {
        return new SettableClock(date);
    }

    @Override
    public Date now() {
        return date(date);
    }

    public void now(Date date) {
        this.date = date(date);
    }
}