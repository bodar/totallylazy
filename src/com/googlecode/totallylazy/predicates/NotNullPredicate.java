package com.googlecode.totallylazy.predicates;

public class NotNullPredicate<T> extends LogicalPredicate<T> {
    public boolean matches(T other) {
        return other != null;
    }
}
