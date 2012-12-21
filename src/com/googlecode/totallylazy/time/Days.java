package com.googlecode.totallylazy.time;

import java.util.Date;

import static java.util.Calendar.DAY_OF_YEAR;

public class Days {
    public static Date add(Date date, int amount) {
        return Dates.add(date, DAY_OF_YEAR, amount);
    }

    public static Date subtract(Date date, int amount) {
        return Dates.subtract(date, DAY_OF_YEAR, amount);
    }
}
