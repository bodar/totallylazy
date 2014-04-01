package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.LogicalPredicate;

public interface Predicate<T> extends java.util.function.Predicate<T> {
    boolean matches(T other);

    default LogicalPredicate<T> and(Predicate<? super T> predicate){
        return Predicates.<T>and(this, predicate);
    }

    default LogicalPredicate<T> or(Predicate<? super T> predicate){
        return Predicates.<T>or(this, predicate);
    }

    default LogicalPredicate<T> not() {
        return Predicates.<T>not(this);
    }

    @Override
    default boolean test(T t) {
        return matches(t);
    }
}
