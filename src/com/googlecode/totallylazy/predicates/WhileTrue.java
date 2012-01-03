package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;

public class WhileTrue<T> extends LogicalPredicate<T> {
    private final Predicate<? super T> predicate;
    private boolean continueMatching = true;

    public WhileTrue(Predicate<? super T> predicate) {
        this.predicate = predicate;
    }

    public boolean matches(T other) {
        if(continueMatching) {
            continueMatching = predicate.matches(other);
            return continueMatching;
        }
        return false;
    }
}
