package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;

public abstract class LogicalPredicate<T> implements Predicate<T> {
    @SuppressWarnings("unchecked")
    public static <T> LogicalPredicate<T> logicalPredicate(Predicate<? super T> predicate) {
        if(predicate instanceof LogicalPredicate){
            return (LogicalPredicate<T>) predicate;
        }
        return new DelegatingPredicate<T>(predicate);
    }

    public LogicalPredicate<T> and(Predicate<? super T> predicate){
        return Predicates.<T>and(this, predicate);
    }

    public LogicalPredicate<T> or(Predicate<? super T> predicate){
        return Predicates.<T>or(this, predicate);
    }

    public LogicalPredicate<T> not() {
        return Predicates.<T>not(this);
    }
}
