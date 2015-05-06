package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Value;


public class Attribute implements Value<String>, Expression {
    private final String value;

    public Attribute(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
