package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Aggregate;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.SelectCallable;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.sql.expressions.Expressions.expression;
import static com.googlecode.totallylazy.records.sql.expressions.SetFunctionType.setFunctionType;

public class SelectList extends CompoundExpression{
    public SelectList(Sequence<Keyword> select) {
        super(select.map(derivedColumn()));
    }

    public static SelectList selectList(final Sequence<Keyword> select) {
        return new SelectList(select);
    }

    @Override
    public String text() {
        return expressions.map(Expressions.text()).toString();
    }

    public static Callable1<Keyword, Expression> derivedColumn() {
        return new Callable1<Keyword, Expression>() {
            public Expression call(Keyword keyword) throws Exception {
                return derivedColumn(keyword);
            }
        };
    }

    public static <T> Expression derivedColumn(Callable1<? super Record, T> callable) {
        if(callable instanceof Aggregate){
            Aggregate aggregate = (Aggregate) callable;
            return setFunctionType(aggregate.callable(), aggregate.source().name()).join(expression(" as " + aggregate.name()));
        }
        if (callable instanceof Keyword) {
            return expression(callable.toString());
        }
        if (callable instanceof SelectCallable) {
            return expression(sequence(((SelectCallable) callable).keywords()).toString("", ",", ""));
        }
        throw new UnsupportedOperationException("Unsupported callable " + callable);
    }


}
