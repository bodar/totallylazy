package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.predicates.ByPredicate;
import com.googlecode.totallylazy.predicates.Is;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;
import static java.util.Arrays.asList;

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
        throw new UnsupportedOperationException("Unsupported callable type " + callable);
    }

    @Override
    public Sequence<Record> filter(Predicate<? super Record> predicate) {
        if(predicate instanceof ByPredicate){
            ByPredicate by = (ByPredicate) predicate;
            if(by.callable() instanceof Keyword && by.predicate() instanceof Is){
                return new QuerySequence(connection, query.where((Keyword) by.callable(), (Is) by.predicate()));
            }
        }

        throw new UnsupportedOperationException("Unsupported callable type " + predicate);
    }

    @Override
    public String toString() {
        return query.toString();
    }
}
