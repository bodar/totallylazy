package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.BinaryPredicate;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Function1;

public abstract class LogicalBinaryPredicate<T> extends Function1<T, LogicalPredicate<T>> implements BinaryPredicate<T>, Callable1<T, LogicalPredicate<T>>, Callable2<T, T, Boolean> {
    @Override
    public Boolean call(T t, T t2) throws Exception {
        return matches(t, t2);
    }

    @Override
    public LogicalPredicate<T> call(final T a) throws Exception {
        return new LogicalPredicate<T>() {
            @Override
            public boolean matches(T b) {
                return LogicalBinaryPredicate.this.matches(a, b);
            }
        };
    }

    public LogicalPredicate<T> applySecond(final T b) {
        return new LogicalPredicate<T>() {
            @Override
            public boolean matches(T a) {
                return LogicalBinaryPredicate.this.matches(a, b);
            }
        };
    }
}
