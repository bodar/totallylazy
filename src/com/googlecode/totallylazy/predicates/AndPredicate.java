package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;

public class AndPredicate<T> implements Predicate<T> {
    private final Predicate<? super T>[] predicates;

    public AndPredicate(Predicate<? super T>... predicates) {
        this.predicates = predicates;
    }

    public boolean matches(T value) {
        for (Predicate<? super T> predicate : predicates) {
            if (!predicate.matches(value)) return false;
        }
        return true;
    }

    public Predicate<? super T>[] predicates() {
        return predicates;
    }
}
