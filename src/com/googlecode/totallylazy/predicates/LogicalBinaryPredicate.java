package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.BinaryPredicate;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Predicate;

public abstract class LogicalBinaryPredicate<T> extends com.googlecode.totallylazy.Eq implements BinaryPredicate<T>, Callable2<T, T, Boolean> {
    public abstract LogicalBinaryPredicate<T> flip();

    public abstract Predicate<T> apply(T t);

    public Predicate<T> applySecond(final T b) {
        return flip().apply(b);
    }

    public Predicate<T> call(T t) throws Exception {
        return apply(t);
    }

    @Override
    public Boolean call(T t, T t2) throws Exception {
        return matches(t, t2);
    }
}
