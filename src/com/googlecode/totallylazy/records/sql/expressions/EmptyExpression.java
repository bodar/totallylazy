package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Strings;

public class EmptyExpression extends AbstractExpression{
    public String text() {
        return Strings.EMPTY;
    }

    public Sequence<Object> parameters() {
        return Sequences.empty();
    }
}
