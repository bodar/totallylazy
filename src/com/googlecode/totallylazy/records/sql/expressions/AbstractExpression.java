package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Callable1;

import static com.googlecode.totallylazy.Callables.returnArgument;

public abstract class AbstractExpression implements Expression {
    public CompoundExpression join(Expression other) {
        return new CompoundExpression(this, other);
    }

    public String toString() {
        return toString(returnArgument());
    }

    public String toString(Callable1<Object, Object> valueConverter) {
        return String.format(text().replace("?", "'%s'"), parameters().map(valueConverter).toArray(Object.class));
    }
}
