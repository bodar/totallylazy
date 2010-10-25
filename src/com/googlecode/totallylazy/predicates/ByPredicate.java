package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callers;
import com.googlecode.totallylazy.Predicate;

public class ByPredicate<T, R> implements Predicate<T> {
    private final Predicate<? super R> predicate;
    private final Callable1<? super T, R> callable;

    public ByPredicate(Predicate<? super R> predicate, Callable1<? super T, R> callable) {
        this.predicate = predicate;
        this.callable = callable;
    }

    public boolean matches(T o) {
        return predicate.matches(Callers.call(callable, o));
    }

    public Predicate<? super R> predicate() {
        return predicate;
    }

    public Callable1<? super T, R> callable() {
        return callable;
    }
}
