package com.googlecode.totallylazy.template.ast;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;

public class FunctionCall implements Expression {
    private final Object name;
    private final Arguments<?> arguments;

    public FunctionCall(final Object name, final Arguments<?> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public Object name() {
        return name;
    }

    public Arguments<?> arguments() {
        return cast(arguments);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + name + '(' +
                sequence(arguments) + "))";
    }
}
