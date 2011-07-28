package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Strings;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Expression extends Pair<String, Sequence<Object>> {
    public Expression(String expression, Sequence<Object> parameters) {
        super(expression, parameters);
    }

    public static Expression expression(String expression, Object... parameters){
        return new Expression(expression, sequence(parameters));
    }

    public static Expression expression(String expression, Sequence<Object> parameters){
        return new Expression(expression, parameters);
    }

    public static Expression empty() {
        return expression(Strings.EMPTY, Sequences.empty());
    }

    public String expression() {
        return first();
    }

    public Sequence<Object> parameters() {
        return second();
    }

    public Expression join(Expression other){
        return expression(expression() + " " + other.expression(), parameters().join(other.parameters()));
    }

    public static Expression join(final Expression... expressions) {
        return join(sequence(expressions));
    }

    public static Expression join(final Sequence<Expression> expressions) {
        return expressions.reduce(join());
    }

    public static Callable2<? super Expression, ? super Expression, Expression> join() {
        return new Callable2<Expression, Expression, Expression>() {
            public Expression call(Expression original, Expression next) throws Exception {
                return original.join(next);
            }
        };
    }

    @Override
    public String toString() {
        return expression() + " " + parameters().toString("(", ",", ")");
    }
}
