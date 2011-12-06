package com.googlecode.totallylazy.predicates;

public class AllPredicate<T> extends LogicalPredicate<T> {
    public boolean matches(T instance) {
        return true;
    }
}
