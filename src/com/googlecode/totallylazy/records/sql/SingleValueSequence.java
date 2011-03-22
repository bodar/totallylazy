package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Queryable;
import com.googlecode.totallylazy.records.Record;

import java.util.Iterator;

class SingleValueSequence<S> extends Sequence<S> implements QuerySequence{
    private final Callable1<? super Record, S> callable;
    private final Queryable queryable;
    private final Query query;

    public SingleValueSequence(final Queryable queryable, final Query query, final Callable1<? super Record, S> callable) {
        this.queryable = queryable;
        this.query = query;
        this.callable = callable;
    }

    public Iterator<S> iterator() {
        return Iterators.map(queryable.query(query), callable);
    }

    public Query query() {
        return query;
    }
}
