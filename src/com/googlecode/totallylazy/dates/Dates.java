package com.googlecode.totallylazy.dates;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.predicates.Between;

import java.util.Date;
import java.util.GregorianCalendar;

public class Dates {
    public static Date date(int year, int month, int day) {
        return new GregorianCalendar(year, month - 1, day).getTime();
    }
}
