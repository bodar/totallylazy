package com.googlecode.totallylazy.dates;

import com.googlecode.totallylazy.Predicate;

import java.util.Date;

public class Dates {
    public static Predicate<Date> after(final Date date) {
        return new AfterPredicate(date);
    }

}
