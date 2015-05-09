package com.googlecode.totallylazy.template.ast;

public class Indirection implements Expression {
    private final Expression expression;

    public Indirection(Expression expression) {
        this.expression = expression;
    }

    public Expression expression() {
        return expression;
    }
}
