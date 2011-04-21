package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;

public class StartsWithPredicate implements Predicate<String> {
    private final String value;

    public StartsWithPredicate(String value) {
        this.value = value;
    }

    public boolean matches(String other) {
        return other.startsWith(value);
    }

    public String value() {
        return value;
    }
}
