package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Predicate;

public class LessThanOrEqualToPredicate implements Predicate<Number> {
    private final Number value;

    public LessThanOrEqualToPredicate(Number value) {
        this.value = value;
    }

    public boolean matches(Number other) {
        return Numbers.lessThanOrEqualTo(other, value);
    }

    public Number value() {
        return value;
    }
}
