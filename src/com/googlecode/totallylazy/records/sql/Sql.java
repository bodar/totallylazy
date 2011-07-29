package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.numbers.Average;
import com.googlecode.totallylazy.numbers.Sum;
import com.googlecode.totallylazy.records.Aggregate;
import com.googlecode.totallylazy.records.CountNotNull;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Maximum;
import com.googlecode.totallylazy.records.Minimum;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.SelectCallable;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.sql.Expression.expression;
import static java.lang.String.format;

public class Sql {
    private Sql() {
    }

    public static <T> Expression toSql(Callable1<? super Record, T> callable) {
        if (callable instanceof Keyword) {
            return expression(callable.toString());
        }
        if (callable instanceof SelectCallable) {
            return expression(sequence(((SelectCallable) callable).keywords()).toString("", ",", ""));
        }
        throw new UnsupportedOperationException("Unsupported callable " + callable);
    }

    public static Callable1<? super Expression, Iterable<Object>> values() {
        return new Callable1<Expression, Iterable<Object>>() {
            public Iterable<Object> call(Expression pair) throws Exception {
                return pair.second();
            }
        };
    }

    public static Expression asSql(Aggregate aggregate) {
        return toSql(aggregate.callable(), aggregate.source().name()).join(expression(" as " + aggregate.name()));
    }

    private static Expression toSql(Callable2<?, ?, ?> callable, String column) {
        if(callable instanceof CountNotNull){
            return expression(format("count(%s)", column));
        }
        if(callable instanceof Average){
            return expression(format("avg(%s)", column));
        }
        if(callable instanceof Sum){
            return expression(format("sum(%s)", column));
        }
        if(callable instanceof Minimum){
            return expression(format("min(%s)", column));
        }
        if(callable instanceof Maximum){
            return expression(format("max(%s)", column));
        }
        throw new UnsupportedOperationException();
    }

}
