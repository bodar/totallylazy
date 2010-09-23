package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;

public class WhileTrue<T> implements Predicate<T> {
    private final Predicate<T> predicate;
    private boolean continueMatching = true;

    public WhileTrue(Predicate<T> predicate) {
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
