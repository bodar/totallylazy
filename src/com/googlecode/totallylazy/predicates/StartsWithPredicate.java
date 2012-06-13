package com.googlecode.totallylazy.predicates;

public class StartsWithPredicate extends LogicalPredicate<String> {
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
