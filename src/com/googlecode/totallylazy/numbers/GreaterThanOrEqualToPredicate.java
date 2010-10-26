package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Predicate;

public class GreaterThanOrEqualToPredicate implements Predicate<Number> {
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
