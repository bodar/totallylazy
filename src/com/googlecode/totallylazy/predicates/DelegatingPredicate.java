package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;

public class DelegatingPredicate<T> extends LogicalPredicate<T> {
    private final Predicate<? super T> predicate;

    public DelegatingPredicate(Predicate<? super T> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean matches(T other) {
        return predicate.matches(other);
    }
}
