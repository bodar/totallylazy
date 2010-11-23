package com.googlecode.totallylazy.dates;

import com.googlecode.totallylazy.predicates.LessThanOrEqualTo;

import java.util.Date;

class LessThanOrEqualToPredicate implements LessThanOrEqualTo<Date> {
    private final Date date;

    public LessThanOrEqualToPredicate(Date date) {
        this.date = date;
    }

    public boolean matches(Date other) {
        return other.before(date) || date.equals(other);
    }

    public Date value() {
        return date;
    }
}
