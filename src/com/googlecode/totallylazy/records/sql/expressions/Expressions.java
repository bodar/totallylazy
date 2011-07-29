package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.Strings;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Expressions {
    public static Callable1<? super Expression, Iterable<Object>> values() {
        return new Callable1<Expression, Iterable<Object>>() {
            public Iterable<Object> call(Expression expression) throws Exception {
                return expression.parameters();
            }
        };
    }

    public static Callable1<? super Expression, String> expression(Class<?> aClass) {
        return new Callable1<Expression, String>() {
            public String call(Expression expression) throws Exception {
                return expression.text();
            }
        };
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
}
