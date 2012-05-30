package com.googlecode.totallylazy.predicates;

public class NotNullPredicate<T> extends LogicalPredicate<T> {
    public boolean matches(T other) {
        return other != null;
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NotNullPredicate;
    }

    @Override
    public String toString() {
        return "is not null";
    }
}
