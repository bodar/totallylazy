package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;

public abstract class LogicalPredicate<T> implements Predicate<T> {
    public AndPredicate<T> and(Predicate<? super T> predicate){
        return Predicates.<T>and(this, predicate);
    }
}
