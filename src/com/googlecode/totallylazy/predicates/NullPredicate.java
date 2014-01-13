package com.googlecode.totallylazy.predicates;

public class NullPredicate<T> extends AbstractPredicate<T> {
    public boolean matches(T other) {
        return other == null;
    }

    @Override
    public int hashCode() {
        return 13;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NullPredicate;
    }

    @Override
    public String toString() {
        return "is null";
    }
}
