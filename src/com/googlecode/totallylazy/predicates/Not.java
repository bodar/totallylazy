package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;

public class Not<T> implements Predicate<T> {
    private final Predicate<? super T> predicate;

    public Not(Predicate<? super T>  predicate) {
        this.predicate = predicate;
    }

    public boolean matches(T other) {
        return !predicate.matches(other);
    }
}
