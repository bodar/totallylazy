package com.googlecode.totallylazy.time;

import java.util.Date;

public class SettableClock implements Clock {
    private volatile Date date;

    public SettableClock() {
        this(Dates.date(2001, 1, 1));
    }

    public SettableClock(Date date) {
        this.date = date;
    }

    @Override
    public Date now() {
        return date;
    }

    public void now(Date date) {
        this.date = date;
    }
}