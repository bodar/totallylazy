package com.googlecode.totallylazy.dates;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.predicates.Between;

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

    public static Between<Date> between(final Date lower, final Date upper) {
        return new BetweenPredicate(lower, upper);
    }

}
