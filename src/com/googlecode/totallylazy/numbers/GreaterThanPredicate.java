package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Predicate;

public class GreaterThanPredicate implements Predicate<Number> {
    private final Number value;

    public GreaterThanPredicate(Number value) {
        this.value = value;
    }

    public boolean matches(Number other) {
        return Numbers.greaterThan(other, value);
    }

    public Number value() {
        return value;
    }
}
