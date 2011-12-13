package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;

public class OrPredicate<T> extends LogicalPredicate<T> {
    private final Predicate<? super T>[] predicates;

    public OrPredicate(Predicate<? super T>... predicates) {
        this.predicates = predicates;
    }

    public boolean matches(T value) {
        for (Predicate<? super T> predicate : predicates) {
            if (predicate.matches(value)) return true;
        }
        return false;
    }

    public Predicate<? super T>[] predicates() {
        return predicates;
    }
}
