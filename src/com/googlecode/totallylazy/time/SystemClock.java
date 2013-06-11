package com.googlecode.totallylazy.time;

import java.util.Date;

public class SystemClock implements Clock {
    public SystemClock() {}

    public static SystemClock systemClock() {
        return new SystemClock();
    }

    public Date now() {
        return new Date();
    }
}
