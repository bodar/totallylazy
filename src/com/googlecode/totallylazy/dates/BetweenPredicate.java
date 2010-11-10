package com.googlecode.totallylazy.dates;

import com.googlecode.totallylazy.predicates.Between;

import java.util.Date;

import static com.googlecode.totallylazy.dates.Dates.greaterThanOrEqualTo;
import static com.googlecode.totallylazy.dates.Dates.lessThanOrEqualTo;

class BetweenPredicate implements Between<Date> {
    private final Date lower;
    private final Date upper;

    BetweenPredicate(Date lower, Date upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public boolean matches(Date other) {
        return greaterThanOrEqualTo(lower).matches(other) && lessThanOrEqualTo(upper).matches(other);
    }

    public Date lower() {
        return lower;
    }

    public Date upper() {
        return upper;
    }
}
