package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;

import static com.googlecode.totallylazy.Unchecked.cast;

public class AlwaysTrue extends AbstractPredicate<Object> {
    private static AlwaysTrue instance = new AlwaysTrue();
    private AlwaysTrue() {}

    public boolean matches(Object instance) {
        return true;
    }

    public static <T> Predicate<T> alwaysTrue() {
        return cast(instance);
    }

    @Override
    public String toString() {
        return "true";
    }
}
