package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Strings;

import static com.googlecode.totallylazy.Sequences.sequence;

public class ParameterisedExpression extends Pair<String, Sequence<Object>> {
    public ParameterisedExpression(String expression, Sequence<Object> parameters) {
        super(expression, parameters);
    }

    public static ParameterisedExpression expression(String expression){
        return new ParameterisedExpression(expression, Sequences.<Object>empty());
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

    public static ParameterisedExpression join(final ParameterisedExpression... expressions) {
        return join(sequence(expressions));
    }

    public static ParameterisedExpression join(final Sequence<ParameterisedExpression> expressions) {
        return expressions.reduce(join());
    }

    public static Callable2<? super ParameterisedExpression, ? super ParameterisedExpression, ParameterisedExpression> join() {
        return new Callable2<ParameterisedExpression, ParameterisedExpression, ParameterisedExpression>() {
            public ParameterisedExpression call(ParameterisedExpression original, ParameterisedExpression next) throws Exception {
                return original.join(next);
            }
        };
    }



    @Override
    public String toString() {
        return expression() + " " + parameters().toString("(", ",", ")");
    }
}
