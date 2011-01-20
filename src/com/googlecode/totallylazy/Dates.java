package com.googlecode.totallylazy;

import java.util.Date;
import java.util.GregorianCalendar;

public class Dates {
    public static Date date(int year, int month, int day) {
        return new GregorianCalendar(year, month - 1, day).getTime();
    }

    public static Date date(int year, int month, int day, int hour, int minute, int second) {
        return new GregorianCalendar(year, month - 1, day, hour, minute, second).getTime();
    }
}
