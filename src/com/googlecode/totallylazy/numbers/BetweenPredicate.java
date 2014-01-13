package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.predicates.Between;
import com.googlecode.totallylazy.predicates.AbstractPredicate;

import static com.googlecode.totallylazy.numbers.Numbers.greaterThanOrEqualTo;
import static com.googlecode.totallylazy.numbers.Numbers.lessThanOrEqualTo;

public class BetweenPredicate extends AbstractPredicate<Number> implements Between<Number> {
    private final Number lower;
    private final Number upper;

    public BetweenPredicate(final Number lower, final Number upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public boolean matches(Number other) {
        return greaterThanOrEqualTo(other, lower) && lessThanOrEqualTo(other, upper);
    }

    public Number lower() {
        return lower;
    }

    public Number upper() {
        return upper;
    }
}
