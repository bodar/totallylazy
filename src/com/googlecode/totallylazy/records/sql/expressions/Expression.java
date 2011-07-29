package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Sequence;

public class Expression {
    private final String text;
    private final Sequence<Object> parameters;

    public Expression(String text, Sequence<Object> parameters) {
        this.text = text;
        this.parameters = parameters;
    }

    public String text() {
        return text;
    }

    public Sequence<Object> parameters() {
        return parameters;
    }

    public Expression join(Expression other){
        return Expressions.expression(text() + " " + other.text(), parameters().join(other.parameters()));
    }

    @Override
    public String toString() {
        return text() + " " + parameters().toString("(", ",", ")");
    }
}
