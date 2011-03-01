package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;

public class Not<T> extends LogicalPredicate<T> {
    private final Predicate<? super T> predicate;

    public Not(Predicate<? super T>  predicate) {
        this.predicate = predicate;
    }

    public boolean matches(T other) {
        return !predicate.matches(other);
    }

    public Predicate<? super T> predicate() {
        return predicate;
    }
}
