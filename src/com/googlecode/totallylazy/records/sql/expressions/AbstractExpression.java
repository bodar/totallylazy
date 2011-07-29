package com.googlecode.totallylazy.records.sql.expressions;

public abstract class AbstractExpression implements Expression {
    public AbstractExpression join(Expression other) {
        return new CompoundExpression(this, other);
    }

    public String toString() {
        return text() + " " + parameters().toString("(", ",", ")");
    }
}
