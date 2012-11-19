package com.googlecode.totallylazy.predicates;

public class EndsWithPredicate extends LogicalPredicate<String> {
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
