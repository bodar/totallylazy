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

    @Override
    public String toString() {
        return String.format("starts with '%s'", value());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof StartsWithPredicate && ((StartsWithPredicate) o).value().equals(value());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
