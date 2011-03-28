package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Queryable;
import com.googlecode.totallylazy.records.Record;

import java.io.PrintStream;
import java.util.Iterator;

import static java.lang.String.format;

class SingleValueSequence<T> extends Sequence<T> implements QuerySequence{
    private final Callable1<? super Record, T> callable;
    private final PrintStream logger;
    private final Queryable queryable;
    private final Query query;

    public SingleValueSequence(final Queryable queryable, final Query query, final Callable1<? super Record, T> callable, final PrintStream logger) {
        this.queryable = queryable;
        this.query = query;
        this.callable = callable;
        this.logger = logger;
    }

    public Iterator<T> iterator() {
        return Iterators.map(queryable.query(query), callable);
    }

    public Query query() {
        return query;
    }

    @Override
    public <S> S reduce(Callable2<? super S, ? super T, S> callable) {
        if(query().sql().isSupported(callable)){
            return (S) queryable.query(query.reduce(callable)).next().fields().head().second();
        }
        logger.println(format("Warning: Unsupported Callable2 %s dropping down to client side sequence functionality", callable));
        return super.reduce(callable);
    }

}
