package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Strings;

public class ParameterisedExpression extends Pair<String, Sequence<Object>> {
    public ParameterisedExpression(String expression, Sequence<Object> parameters) {
        super(expression, parameters);
    }

    public static ParameterisedExpression expression(String expression, Sequence<Object> parameters){
        return new ParameterisedExpression(expression, parameters);
    }

    public static ParameterisedExpression empty() {
        return expression(Strings.EMPTY, Sequences.empty());
    }

    public String expression() {
        return first();
    }

    public Sequence<Object> parameters() {
        return second();
    }

    public ParameterisedExpression join(ParameterisedExpression other){
        return expression(expression() + " " + other.expression(), parameters().join(other.parameters()));
    }

    @Override
    public String toString() {
        return expression() + " " + parameters().toString("(", ",", ")");
    }
}
