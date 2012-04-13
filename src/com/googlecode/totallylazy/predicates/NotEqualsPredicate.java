package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Value;

public class NotEqualsPredicate<T> extends LogicalPredicate<T> implements Value<T> {
    private final T value;

    public NotEqualsPredicate(T value) {
        this.value = value;
    }

    public boolean matches(T other) {
        return !value.equals(other);
    }

    public T value() {
        return value;
    }
}
