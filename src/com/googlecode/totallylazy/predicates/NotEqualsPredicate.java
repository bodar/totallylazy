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

    @Override
    public int hashCode() {
        return 31 * value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NotEqualsPredicate && value.equals(((NotEqualsPredicate) obj).value());
    }

    @Override
    public String toString() {
        return "is not " + value;
    }
}
