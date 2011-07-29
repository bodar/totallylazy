package com.googlecode.totallylazy.records.sql.expressions;

public abstract class AbstractExpression implements Expression {
    public AbstractExpression join(Expression other) {
        return new CompoundExpression(this, other);
    }

    public String toString() {
        return String.format(text().replace("?", "'%s'"), parameters().toArray(Object.class));
    }
}
