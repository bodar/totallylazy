package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicate;

public class ContainsPredicate implements Predicate<String> {
    private final String value;

    public ContainsPredicate(String value) {
        this.value = value;
    }

    public boolean matches(String other) {
        return other.contains(value);
    }

    public String value() {
        return value;
    }
}
