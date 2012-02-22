package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callers;
import com.googlecode.totallylazy.Predicate;

import static com.googlecode.totallylazy.Unchecked.cast;

public class WherePredicate<T, R> extends LogicalPredicate<T> {
    private final Callable1<? super T, ? extends R> callable;
    private final Predicate<? super R> predicate;

    private WherePredicate(final Callable1<? super T, ? extends R> callable, final Predicate<? super R> predicate) {
        this.predicate = predicate;
        this.callable = callable;
    }

    public static <T, R> WherePredicate<T, R> where(final Callable1<? super T, ? extends R> callable, final Predicate<? super R> predicate) {
        return new WherePredicate<T, R>(callable, predicate);
    }

    public boolean matches(T o) {
        return predicate.matches(Callers.call(callable, o));
    }

    public Callable1<T, R> callable() {
        return cast(callable);
    }

    public Predicate<R> predicate() {
        return cast(predicate);
    }
}
