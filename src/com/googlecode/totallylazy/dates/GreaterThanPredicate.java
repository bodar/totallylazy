package com.googlecode.totallylazy.dates;

import com.googlecode.totallylazy.predicates.GreaterThan;

import java.util.Date;

class GreaterThanPredicate implements GreaterThan<Date> {
    private final Date date;

    public GreaterThanPredicate(Date date) {
        this.date = date;
    }

    public boolean matches(Date other) {
        return other.after(date);
    }

    public Date value() {
        return date;
    }
}
