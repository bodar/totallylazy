package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Sequence;

import java.util.Iterator;

class SingleValueSequence<S> extends Sequence<S> implements QuerySequence{
    private final Callable1<? super Record, S> callable;
    private final Query query;

    public SingleValueSequence(final Query query, final Callable1<? super Record, S> callable) {
        this.query = query;
        this.callable = callable;
    }

    public Iterator<S> iterator() {
        return Iterators.map(new RecordIterator(query), callable);
    }

    public Query query() {
        return query;
    }
}
