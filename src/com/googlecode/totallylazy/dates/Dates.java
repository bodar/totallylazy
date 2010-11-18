package com.googlecode.totallylazy.dates;

import java.util.Date;
import java.util.GregorianCalendar;

public class Dates {
    public static Date date(int year, int month, int day) {
        return new GregorianCalendar(year, month - 1, day).getTime();
    }
}
