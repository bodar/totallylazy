package com.googlecode.totallylazy.matchers;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.numbers.Numbers;
import com.googlecode.totallylazy.predicates.LogicalPredicate;

public class NumberMatcher extends LogicalPredicate<Number> {
    private final Number other;

    public NumberMatcher(Number other) {
        this.other = other;
    }

    @Override
    public boolean matches(Number number) {
        return Numbers.equalTo(number, other);
    }

    public static Predicate<Number> is(final Number other) {
        return new NumberMatcher(other);
    }

    public static Predicate<Number> equalTo(final Number other) {
        return is(other);
    }


}
