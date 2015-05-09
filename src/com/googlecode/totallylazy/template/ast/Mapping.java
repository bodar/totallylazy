package com.googlecode.totallylazy.template.ast;

public class Mapping implements Expression {
    private final Attribute attribute;
    private final Anonymous expression;

    public Mapping(Attribute attribute, Anonymous expression) {
        this.attribute = attribute;
        this.expression = expression;
    }

    public Attribute attribute() {
        return attribute;
    }

    public Anonymous expression() {
        return expression;
    }
}
