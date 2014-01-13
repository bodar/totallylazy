package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.predicates.LessThanOrEqualTo;
import com.googlecode.totallylazy.predicates.AbstractPredicate;

public class LessThanOrEqualToPredicate extends AbstractPredicate<Number> implements LessThanOrEqualTo<Number> {
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
