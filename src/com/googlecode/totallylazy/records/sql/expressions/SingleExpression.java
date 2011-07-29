package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Sequence;

public class SingleExpression extends AbstractExpression {
    private final String text;
    private final Sequence<Object> parameters;

    public SingleExpression(String text, Sequence<Object> parameters) {
        this.text = text;
        this.parameters = parameters;
    }

    public String text() {
        return text;
    }

    public Sequence<Object> parameters() {
        return parameters;
    }

}
