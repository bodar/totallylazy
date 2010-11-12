package com.googlecode.totallylazy.dates;

import com.googlecode.totallylazy.predicates.GreaterThanOrEqualTo;

import java.util.Date;

class GreaterThanOrEqualToPredicate implements GreaterThanOrEqualTo<Date> {
    private final Date date;

    public GreaterThanOrEqualToPredicate(Date date) {
        this.date = date;
    }

    public boolean matches(Date other) {
        return other.after(date) || date.equals(other);
    }

    public Date value() {
        return date;
    }
}
