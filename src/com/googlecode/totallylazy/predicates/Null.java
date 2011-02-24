package com.googlecode.totallylazy.predicates;

public class Null<T> extends LogicalPredicate<T> {
    public boolean matches(T other) {
        return other == null;
    }
}
