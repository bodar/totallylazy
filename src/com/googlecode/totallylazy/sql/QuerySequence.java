package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;

import java.sql.Connection;
import java.util.Iterator;

import static com.googlecode.totallylazy.Sequences.sequence;

public class QuerySequence extends Sequence<Record> {
    private final Connection connection;
    private final Query query;

    public QuerySequence(Connection connection, Query query) {
        this.connection = connection;
        this.query = query;
    }

    public Iterator<Record> iterator() {
        return new RecordIterator(connection, query);
    }

    @Override
    public <S> Sequence<S> map(final Callable1<? super Record, S> callable) {
        if(callable instanceof Keyword){
            return new Sequence<S>() {
                public Iterator<S> iterator() {
                    return Iterators.map(new RecordIterator(connection, query.select((Keyword) callable)), callable);
                }
            };
        }
        if(callable instanceof KeywordsCallable){
            return (Sequence<S>) new QuerySequence(connection, query.select(((KeywordsCallable) callable).keywords()));
        }
        System.out.println(String.format("Warning: unsupported callables %s dropping down to client side sequence functionality", callable));
        return super.map(callable);
    }

    @Override
    public Sequence<Record> filter(Predicate<? super Record> predicate) {
        if(query.isSupported(predicate)){
            return new QuerySequence(connection, query.where(predicate));
        }
        System.out.println(String.format("Warning: unsupported predicate %s dropping down to client side sequence functionality", predicate));
        return super.filter(predicate);
    }

    @Override
    public String toString() {
        return query.toString();
    }
}
