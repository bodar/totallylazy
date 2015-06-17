package com.googlecode.totallylazy.time;

import java.util.Date;

/** @deprecated Replaced by {@link StoppedClock}  } */
@Deprecated
public class FixedClock extends StoppedClock {
    public FixedClock(Date date) {
        super(date);
    }
}
