package com.googlecode.totallylazy.predicates;

public class WhileTrue<T> extends LogicalPredicate<T> {
    private final Predicate<? super T> predicate;
    private volatile boolean continueMatching = true;

    public WhileTrue(Predicate<? super T> predicate) {
        this.predicate = predicate;
    }

    public boolean matches(T other) {
        return continueMatching && (continueMatching = predicate.matches(other));
    }
}
