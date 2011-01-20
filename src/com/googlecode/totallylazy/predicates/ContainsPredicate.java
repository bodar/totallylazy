package com.googlecode.totallylazy.predicates;

public class ContainsPredicate extends LogicalPredicate<String> {
    private final String value;

    public ContainsPredicate(String value) {
        this.value = value;
    }

    public boolean matches(String other) {
        return other.contains(value);
    }

    public CharSequence value() {
        return value;
    }
}
