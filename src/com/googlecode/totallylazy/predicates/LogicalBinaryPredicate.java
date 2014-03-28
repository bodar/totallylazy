package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.BinaryPredicate;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Function1;

public abstract class LogicalBinaryPredicate<T> implements Function1<T, LogicalPredicate<T>>, BinaryPredicate<T>, Callable2<T, T, Boolean> {
    public abstract LogicalBinaryPredicate<T> flip();

    @Override
    public abstract LogicalPredicate<T> apply(T t);

    public LogicalPredicate<T> applySecond(final T b) {
        return flip().apply(b);
    }

    @Override
    public LogicalPredicate<T> call(T t) throws Exception {
        return apply(t);
    }

    @Override
    public Boolean call(T t, T t2) throws Exception {
        return matches(t, t2);
    }
}
