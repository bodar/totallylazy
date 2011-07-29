package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sets;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.SelectCallable;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

import static java.lang.String.format;

public class RecordSequence extends Sequence<Record> implements QuerySequence {
    private final SqlRecords sqlRecords;
    private final SqlQuery sqlQuery;
    private final PrintStream logger;

    public RecordSequence(final SqlRecords records, final SqlQuery sqlQuery, final PrintStream logger) {
        this.sqlRecords = records;
        this.sqlQuery = sqlQuery;
        this.logger = logger;
    }

    public Iterator<Record> iterator() {
        return execute(sqlQuery);
    }

    private Iterator<Record> execute(final SqlQuery sqlQuery) {
        return sqlRecords.iterator(sqlQuery.expression(), sqlQuery.select());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S> Sequence<S> map(final Callable1<? super Record, S> callable) {
        if (callable instanceof Keyword) {
            return new SingleValueSequence<S>(sqlRecords, sqlQuery.select((Keyword) callable), callable, logger);
        }
        if (callable instanceof SelectCallable) {
            return (Sequence<S>) new RecordSequence(sqlRecords, sqlQuery.select(((SelectCallable) callable).keywords()), logger);
        }
        logger.println(format("Warning: Unsupported Callable1 %s dropping down to client side sequence functionality", callable));
        return super.map(callable);
    }

    @Override
    public Sequence<Record> filter(Predicate<? super Record> predicate) {
        try {
            return new RecordSequence(sqlRecords, sqlQuery.where(predicate), logger);
        } catch (UnsupportedOperationException ex) {
            logger.println(format("Warning: Unsupported Predicate %s dropping down to client side sequence functionality", predicate));
            return super.filter(predicate);
        }
    }

    @Override
    public Sequence<Record> sortBy(Comparator<? super Record> comparator) {
        try {
            return new RecordSequence(sqlRecords, sqlQuery.orderBy(comparator), logger);
        } catch (UnsupportedOperationException ex) {
            logger.println(format("Warning: Unsupported Comparator %s dropping down to client side sequence functionality", comparator));
            return super.sortBy(comparator);
        }
    }

    @Override
    public <S> S reduce(Callable2<? super S, ? super Record, S> callable) {
        try {
            SqlQuery query = sqlQuery.reduce(callable);
            return (S) sqlRecords.query(query.expression(), query.select()).head();
        } catch (UnsupportedOperationException ex) {
            logger.println(format("Warning: Unsupported Callable2 %s dropping down to client side sequence functionality", callable));
            return super.reduce(callable);
        }
    }

    @Override
    public Number size() {
        SqlQuery count = sqlQuery.count();
        return (Number) sqlRecords.query(count.expression(), count.select()).head().fields().head().second();
    }

    @Override
    public <S extends Set<Record>> S toSet(S set) {
        return Sets.set(set, execute(sqlQuery.distinct()));
    }

    @Override
    public String toString() {
        return sqlQuery.toString();
    }

    public SqlQuery query() {
        return sqlQuery;
    }
}
