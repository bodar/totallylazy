package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.BinaryPredicate;
import com.googlecode.totallylazy.BiFunction;
import com.googlecode.totallylazy.Predicate;

public abstract class LogicalBinaryPredicate<T> extends com.googlecode.totallylazy.Eq implements BinaryPredicate<T> {
    public abstract LogicalBinaryPredicate<T> flip();

    public abstract Predicate<T> apply(T t);

    public Predicate<T> applySecond(final T b) {
        return flip().apply(b);
    }

    public Predicate<T> call(T t) throws Exception {
        return apply(t);
    }

    public BiFunction<T, T, Boolean> function() {
        return this::matches;
    }
}
