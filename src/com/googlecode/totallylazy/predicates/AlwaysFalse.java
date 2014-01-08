package com.googlecode.totallylazy.predicates;

import static com.googlecode.totallylazy.Unchecked.cast;

public class AlwaysFalse extends LogicalPredicate<Object> {
    private static AlwaysFalse instance = new AlwaysFalse();
    private AlwaysFalse() {}

    public boolean matches(Object instance) {
        return false;
    }

    public static <T> LogicalPredicate<T> alwaysFalse() {
        return cast(instance);
    }

    @Override
    public String toString() {
        return "false";
    }

}
