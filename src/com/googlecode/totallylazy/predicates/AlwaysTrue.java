package com.googlecode.totallylazy.predicates;

import static com.googlecode.totallylazy.Unchecked.cast;

public class AlwaysTrue extends LogicalPredicate<Object> {
    private static AlwaysTrue instance = new AlwaysTrue();
    private AlwaysTrue() {}

    public boolean matches(Object instance) {
        return true;
    }

    public static <T> LogicalPredicate<T> alwaysTrue() {
        return cast(instance);
    }

    @Override
    public String toString() {
        return "true";
    }
}
