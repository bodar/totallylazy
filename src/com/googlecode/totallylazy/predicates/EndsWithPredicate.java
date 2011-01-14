package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;

public class EndsWithPredicate implements Predicate<String> {
    private final String value;

    public EndsWithPredicate(String value) {
        this.value = value;
    }

    public boolean matches(String other) {
        return other.endsWith(value);
    }

    public String value() {
        return value;
    }
}
