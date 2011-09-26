package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

public class TextOnlyExpression extends AbstractExpression {
    private final String text;

    public TextOnlyExpression(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }

    public Sequence<Object> parameters() {
        return Sequences.empty();
    }

}
