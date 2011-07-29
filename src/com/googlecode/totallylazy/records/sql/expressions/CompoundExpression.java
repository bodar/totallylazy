package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Sequences.sequence;

public class CompoundExpression extends AbstractExpression {
    protected final Sequence<Expression> expressions;

    public CompoundExpression(Expression... expressions) {
        this.expressions = sequence(expressions);
    }

    public CompoundExpression(Sequence<Expression> expressions) {
        this.expressions = expressions;
    }

    public String text() {
        return expressions.map(Expressions.text()).toString(" ");
    }

    public Sequence<Object> parameters() {
        return expressions.flatMap(Expressions.parameters());
    }

}
