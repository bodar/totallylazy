package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Sequence;

public class Expression {
    private final String expression;
    private final Sequence<Object> parameters;

    public Expression(String expression, Sequence<Object> parameters) {
        this.expression = expression;
        this.parameters = parameters;
    }

    public String expression() {
        return expression;
    }

    public Sequence<Object> parameters() {
        return parameters;
    }

    public Expression join(Expression other){
        return Expressions.expression(expression() + " " + other.expression(), parameters().join(other.parameters()));
    }

    @Override
    public String toString() {
        return expression() + " " + parameters().toString("(", ",", ")");
    }
}
