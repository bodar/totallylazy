package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Expressions {
    public static Callable1<? super Expression, Iterable<Object>> parameters() {
        return new Callable1<Expression, Iterable<Object>>() {
            public Iterable<Object> call(Expression expression) throws Exception {
                return expression.parameters();
            }
        };
    }

    public static Callable1<? super Expression, String> text() {
        return new Callable1<Expression, String>() {
            public String call(Expression expression) throws Exception {
                return expression.text();
            }
        };
    }

    public static Expression expression(String expression, Object... parameters){
        if(parameters.length == 0) {
            return textOnly(expression);
        }
        return new TextAndParametersExpression(expression, sequence(parameters));
    }

    public static TextOnlyExpression textOnly(Object expression) {
        return new TextOnlyExpression(expression.toString());
    }

    public static Expression expression(String expression, Sequence<Object> parameters){
        if(parameters.isEmpty()) {
            return textOnly(expression);
        }
        return new TextAndParametersExpression(expression, parameters);
    }

    public static EmptyExpression empty() {
        return new EmptyExpression();
    }

    public static CompoundExpression join(final Expression... expressions) {
        return new CompoundExpression(expressions);
    }

    public static CompoundExpression join(final Sequence<Expression> expressions) {
        return new CompoundExpression(expressions);
    }

    public static CompoundExpression join(final Sequence<Expression> expressions, final String start, final String separator, final String end) {
        return new CompoundExpression(expressions, start, separator, end);
    }

}
