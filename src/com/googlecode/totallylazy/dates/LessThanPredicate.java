package com.googlecode.totallylazy.dates;

import com.googlecode.totallylazy.predicates.GreaterThan;
import com.googlecode.totallylazy.predicates.LessThan;

import java.util.Date;

class LessThanPredicate implements LessThan<Date> {
    private final Date date;

    public LessThanPredicate(Date date) {
        this.date = date;
    }

    public boolean matches(Date other) {
        return other.before(date);
    }

    public Date value() {
        return date;
    }
}
