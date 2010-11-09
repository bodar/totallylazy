package com.googlecode.totallylazy.dates;

import com.googlecode.totallylazy.predicates.GreaterThan;

import java.util.Date;

class AfterPredicate implements GreaterThan<Date> {
    private final Date date;

    public AfterPredicate(Date date) {
        this.date = date;
    }

    public boolean matches(Date other) {
        return other.after(date);
    }

    public Date value() {
        return date;
    }
}
