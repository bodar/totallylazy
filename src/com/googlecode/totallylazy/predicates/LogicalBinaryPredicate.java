package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.BinaryPredicate;
import com.googlecode.totallylazy.Function2;

public abstract class LogicalBinaryPredicate<T> extends Function2<T, T, Boolean> implements BinaryPredicate<T> {
    @Override
    public Boolean call(T t, T t2) throws Exception {
        return matches(t, t2);
    }

    @Override
    public LogicalPredicate<T> apply(T t) {
        return LogicalPredicate.logicalPredicate(super.apply(t));
    }

    @Override
    public LogicalPredicate<T> applySecond(T t) {
        return LogicalPredicate.logicalPredicate(super.applySecond(t));
    }

    @Override
    public LogicalBinaryPredicate<T> flip() {
        return BinaryPredicate.constructors.binary(super.flip());
    }
}
