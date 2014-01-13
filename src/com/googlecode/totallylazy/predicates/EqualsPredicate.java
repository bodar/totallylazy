package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.Value;
import com.googlecode.totallylazy.annotations.multimethod;

import static java.lang.String.format;

public class EqualsPredicate<T> extends AbstractPredicate<T> implements Value<T> {
    private final T value;

    private EqualsPredicate(T value) {
        this.value = value;
    }

    public static <T> Predicate<T> equalTo(T value) {
        if (value == null) return new NullPredicate<T>();
        return new EqualsPredicate<T>(value);
    }

    public static <T> Predicate<T> is(T value) {
        return equalTo(value);
    }

    public boolean matches(T other) {
        return value.equals(other);
    }

    public T value() {
        return value;
    }

    @multimethod
    public boolean equals(EqualsPredicate other) {
        return value.equals(other.value());
    }

    @Override
    public int hashCode() {
        return 31 * value.hashCode();
    }

    @Override
    public String toString() {
        return format("is '%s'", Strings.asString(value));
    }
}

