package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Sequence;

public class ParameterisedExpression {
    private final String expression;
    private final Sequence<Object> parameters;

    public ParameterisedExpression(String expression, Sequence<Object> parameters) {
        this.expression = expression;
        this.parameters = parameters;
    }

    public String expression() {
        return expression;
    }

    public Sequence<Object> parameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return expression + " " + parameters.toString("(", ",", ")");
    }
}
