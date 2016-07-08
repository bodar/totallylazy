package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Objects;
import com.googlecode.totallylazy.Value;
import com.googlecode.totallylazy.annotations.multimethod;

public class Name implements Expression, Value<String> {
    private final String value;

    private Name(String value) { this.value = value; }

    public static Name name(String value) {return new Name(value);}

    @Override
    public String value() {
        return value;
    }

    @multimethod
    @Override
    public boolean equals(Object other) {
        return other instanceof Name && Objects.equalTo(value, ((Name) other).value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
