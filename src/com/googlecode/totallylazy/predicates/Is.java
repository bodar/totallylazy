package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;

public class Is<T> implements Predicate<T> {
    private final T t;

    public Is(T t) {
        this.t = t;
    }

    public boolean matches(T other) {
        return other.equals(t);
    }
}
