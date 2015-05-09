package com.googlecode.totallylazy.template.ast;

public class Mapping implements Expression {
    private final Attribute attribute;
    private final AnonymousTemplate expression;

    public Mapping(Attribute attribute, AnonymousTemplate expression) {
        this.attribute = attribute;
        this.expression = expression;
    }

    public Attribute attribute() {
        return attribute;
    }

    public AnonymousTemplate expression() {
        return expression;
    }
}
