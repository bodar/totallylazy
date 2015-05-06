package com.googlecode.totallylazy.template.ast;

import static com.googlecode.totallylazy.Unchecked.cast;

public class FunctionCall implements Expression {
    private final String name;
    private final Object arguments;

    public FunctionCall(final String name, final Object arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public String name() {
        return name;
    }

    public <T> T arguments() {
        return cast(arguments);
    }
}
