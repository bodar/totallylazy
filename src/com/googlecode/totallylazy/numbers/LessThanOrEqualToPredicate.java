package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.predicates.LessThanOrEqualTo;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

public class LessThanOrEqualToPredicate extends LogicalPredicate<Number> implements LessThanOrEqualTo<Number> {
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
