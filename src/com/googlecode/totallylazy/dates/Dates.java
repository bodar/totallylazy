package com.googlecode.totallylazy.dates;

import com.googlecode.totallylazy.Predicate;

import java.util.Date;
import java.util.GregorianCalendar;

public class Dates {
    public static Date date(int year, int month, int day) {
        return new GregorianCalendar(year, month - 1, day).getTime();
    }

    public static Predicate<Date> greaterThan(final Date date) {
        return new GreaterThanPredicate(date);
    }

    public static Predicate<Date> greaterThanOrEqualTo(final Date date) {
        return new GreaterThanOrEqualToPredicate(date);
    }

    public static Predicate<Date> lessThan(final Date date) {
        return new LessThanPredicate(date);
    }

    public static Predicate<Date> lessThanOrEqualTo(final Date date) {
        return new LessThanOrEqualToPredicate(date);
    }

}
