package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.BinaryPredicate;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Function;

public abstract class LogicalBinaryPredicate<T> extends com.googlecode.totallylazy.Eq implements BinaryPredicate<T>, Callable2<T, T, Boolean>, Function<T, AbstractPredicate<T>> {
    public abstract LogicalBinaryPredicate<T> flip();

    @Override
    public abstract AbstractPredicate<T> apply(T t);

    public AbstractPredicate<T> applySecond(final T b) {
        return flip().apply(b);
    }

    @Override
    public AbstractPredicate<T> call(T t) throws Exception {
        return apply(t);
    }

    @Override
    public Boolean call(T t, T t2) throws Exception {
        return matches(t, t2);
    }
}
