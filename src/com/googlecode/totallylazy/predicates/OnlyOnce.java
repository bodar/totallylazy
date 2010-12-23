package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;

public class OnlyOnce<T> extends LogicalPredicate<T> {
    private final Predicate<? super T> predicate;
    private boolean matched = false;

    public OnlyOnce(Predicate<? super T> predicate) {
        this.predicate = predicate;
    }

    public boolean matches(T other) {
        if(matched){
            return false;
        }

        boolean currentMatch = predicate.matches(other);
        if(currentMatch){
            matched = true;
        }
        return currentMatch;
    }
}
