package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.predicates.GreaterThanOrEqualTo;

public class GreaterThanOrEqualToPredicate implements GreaterThanOrEqualTo<Number> {
    private final Number value;

    public GreaterThanOrEqualToPredicate(Number value) {
        this.value = value;
    }

    public boolean matches(Number other) {
        return Numbers.greaterThanOrEqualTo(other, value);
    }

    public Number value() {
        return value;
    }
}
