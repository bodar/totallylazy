package com.googlecode.totallylazy.template.ast;

public class Mapping implements Expression {
    private final Attribute attribute;
    private final Expression expression;

    public Mapping(Attribute attribute, Expression expression) {
        this.attribute = attribute;
        this.expression = expression;
    }

    public Attribute attribute() {
        return attribute;
    }

    public Expression expression() {
        return expression;
    }
}
