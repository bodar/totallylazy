package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sets;
import com.googlecode.totallylazy.records.Queryable;
import com.googlecode.totallylazy.records.Record;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Set;

import static java.lang.String.format;

class SingleValueSequence<T> extends Sequence<T> implements QuerySequence{
    private final Callable1<? super Record, T> callable;
    private final PrintStream logger;
    private final Queryable queryable;
    private final SqlQuery sqlQuery;

    public SingleValueSequence(final Queryable queryable, final SqlQuery sqlQuery, final Callable1<? super Record, T> callable, final PrintStream logger) {
        this.queryable = queryable;
        this.sqlQuery = sqlQuery;
        this.callable = callable;
        this.logger = logger;
    }

    public Iterator<T> iterator() {
        return execute(sqlQuery);
    }

    private Iterator<T> execute(final SqlQuery sqlQuery) {
        return Iterators.map(queryable.query(sqlQuery.parameterisedExpression()), callable);
    }

    public SqlQuery query() {
        return sqlQuery;
    }

    @Override
    public <S> S reduce(Callable2<? super S, ? super T, S> callable) {
        if(query().sql().isSupported(callable)){
            return (S) queryable.query(sqlQuery.reduce(callable).parameterisedExpression()).next().fields().head().second();
        }
        logger.println(format("Warning: Unsupported Callable2 %s dropping down to client side sequence functionality", callable));
        return super.reduce(callable);
    }

    @Override
    public <S extends Set<T>> S toSet(S set) {
        return Sets.set(set, execute(sqlQuery.distinct()));
    }
}
