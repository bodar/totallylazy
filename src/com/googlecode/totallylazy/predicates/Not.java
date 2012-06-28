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

    @Override
    public int hashCode() {
        return 31 * predicate.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Not && predicate.equals(((Not) obj).predicate());
    }

    @Override
    public String toString() {
        return "not " + predicate().toString();
    }
}
