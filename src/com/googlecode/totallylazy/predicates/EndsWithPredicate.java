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

    @Override
    public String toString() {
        return String.format("ends with '%s'", value());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof EndsWithPredicate && ((EndsWithPredicate) o).value().equals(value());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
