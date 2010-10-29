package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Iterator;

import static com.googlecode.totallylazy.Callables.ascending;

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
        return super.filter(predicate);
    }

    @Override
    public Sequence<Record> sortBy(Callable1<Record, ? extends Comparable> callable) {
        return sortBy(ascending(callable));
    }

    @Override
    public Sequence<Record> sortBy(Comparator<? super Record> comparator) {
        if(query.isSupported(comparator)){
            return new QuerySequence(connection, query.orderBy(comparator));
        }
        return super.sortBy(comparator);
    }

    @Override
    public Number size() {
        try {
            final Query count = query.count();
            System.out.println(count);
            final ResultSet resultSet = count.execute(connection);
            resultSet.next();
            final int result = resultSet.getInt(1);
            resultSet.close();
            return result;
        } catch (SQLException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public String toString() {
        return query.toString();
    }
}
