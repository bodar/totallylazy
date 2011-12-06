package com.googlecode.totallylazy.time;

import java.util.Date;

public class FixedClock implements Clock{
    private final Date date;

    public FixedClock(Date date) {
        this.date = date;
    }

    public Date now() {
        return date;
    }
}
