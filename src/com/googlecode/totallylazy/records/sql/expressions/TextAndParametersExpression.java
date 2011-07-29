package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Sequence;

public class TextAndParametersExpression extends AbstractExpression {
    private final String text;
    private final Sequence<Object> parameters;

    public TextAndParametersExpression(String text, Sequence<Object> parameters) {
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
