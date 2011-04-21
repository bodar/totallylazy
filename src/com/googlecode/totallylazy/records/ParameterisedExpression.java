package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Sequence;

public class ParameterisedExpression {
    private final Sequence<Keyword> keywords;
    private final String expression;
    private final Sequence<Object> parameters;

    public ParameterisedExpression(Sequence<Keyword> keywords, String expression, Sequence<Object> parameters) {
        this.keywords = keywords;
        this.expression = expression;
        this.parameters = parameters;
    }

    public Sequence<Keyword> keywords() {
        return keywords;
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
