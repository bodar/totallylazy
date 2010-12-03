package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.SelectCallable;
import com.googlecode.totallylazy.records.Record;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Iterator;

import static com.googlecode.totallylazy.Callables.ascending;

public class RecordSequence extends Sequence<Record> implements QuerySequence {
    private final Query query;

    public RecordSequence(final Query query) {
        this.query = query;
    }

    public Iterator<Record> iterator() {
        return new RecordIterator(query);
    }

    @Override
    public <S> Sequence<S> map(final Callable1<? super Record, S> callable) {
        if(callable instanceof Keyword){
            return new SingleValueSequence<S>(query.select((Keyword) callable), callable);
        }
        if(callable instanceof SelectCallable){
            return (Sequence<S>) new RecordSequence(query.select(((SelectCallable) callable).keywords()));
        }
        System.out.println(String.format("Warning: unsupported callables %s dropping down to client side sequence functionality", callable));
        return super.map(callable);
    }

    @Override
    public Sequence<Record> filter(Predicate<? super Record> predicate) {
        if(query.sql().isSupported(predicate)){
            return new RecordSequence(query.where(predicate));
        }
        return super.filter(predicate);
    }

    @Override
    public Sequence<Record> sortBy(Callable1<? super Record, ? extends Comparable> callable) {
        return sortBy(ascending(callable));
    }

    @Override
    public Sequence<Record> sortBy(Comparator<? super Record> comparator) {
        if(query.sql().isSupported(comparator)){
            return new RecordSequence(query.orderBy(comparator));
        }
        return super.sortBy(comparator);
    }

    @Override
    public Number size() {
        try {
            final Query count = query.count();
            System.out.println(count);
            final ResultSet resultSet = count.execute();
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

    public Query query() {
        return query;
    }
}
