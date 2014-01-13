package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;

import static com.googlecode.totallylazy.Unchecked.cast;

public class AlwaysFalse extends AbstractPredicate<Object> {
    private static AlwaysFalse instance = new AlwaysFalse();
    private AlwaysFalse() {}

    public boolean matches(Object instance) {
        return false;
    }

    public static <T> Predicate<T> alwaysFalse() {
        return cast(instance);
    }

    @Override
    public String toString() {
        return "false";
    }

}
