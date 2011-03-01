package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callers;
import com.googlecode.totallylazy.Predicate;

public class WherePredicate<T, R> extends LogicalPredicate<T> {
    private final Callable1<? super T, R> callable;
    private final Predicate<? super R> predicate;

    public WherePredicate(Callable1<? super T, R> callable, Predicate<? super R> predicate) {
        this.predicate = predicate;
        this.callable = callable;
    }

    public boolean matches(T o) {
        return predicate.matches(Callers.call(callable, o));
    }

    public Callable1<? super T, R> callable() {
        return callable;
    }

    public Predicate<? super R> predicate() {
        return predicate;
    }
}
