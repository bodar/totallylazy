package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.predicates.LessThan;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

public class LessThanPredicate extends LogicalPredicate<Number> implements LessThan<Number> {
    private final Number value;

    public LessThanPredicate(Number value) {
        this.value = value;
    }

    public boolean matches(Number other) {
        return Numbers.lessThan(other, value);
    }

    public Number value() {
        return value;
    }
}
