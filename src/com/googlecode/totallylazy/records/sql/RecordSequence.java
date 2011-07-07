package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.*;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Queryable;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.SelectCallable;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

import static com.googlecode.totallylazy.records.CountNotNull.count;
import static java.lang.String.format;

public class RecordSequence extends Sequence<Record> implements QuerySequence {
    private final Queryable queryable;
    private final SqlQuery sqlQuery;
    private final PrintStream logger;

    public RecordSequence(final Queryable queryable, final SqlQuery sqlQuery, final PrintStream logger) {
        this.queryable = queryable;
        this.sqlQuery = sqlQuery;
        this.logger = logger;
    }

    public Iterator<Record> iterator() {
        return execute(sqlQuery);
    }

    private Iterator<Record> execute(final SqlQuery sqlQuery) {
        return queryable.query(sqlQuery.parameterisedExpression());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S> Sequence<S> map(final Callable1<? super Record, S> callable) {
        if (callable instanceof Keyword) {
            return new SingleValueSequence<S>(queryable, sqlQuery.select((Keyword) callable), callable, logger);
        }
        if (callable instanceof SelectCallable) {
            return (Sequence<S>) new RecordSequence(queryable, sqlQuery.select(((SelectCallable) callable).keywords()), logger);
        }
        logger.println(format("Warning: Unsupported Callable1 %s dropping down to client side sequence functionality", callable));
        return super.map(callable);
    }

    @Override
    public Sequence<Record> filter(Predicate<? super Record> predicate) {
        if (sqlQuery.sql().isSupported(predicate)) {
            return new RecordSequence(queryable, sqlQuery.where(predicate), logger);
        }
        logger.println(format("Warning: Unsupported Predicate %s dropping down to client side sequence functionality", predicate));
        return super.filter(predicate);
    }

    @Override
    public Sequence<Record> sortBy(Comparator<? super Record> comparator) {
        if (sqlQuery.sql().isSupported(comparator)) {
            return new RecordSequence(queryable, sqlQuery.orderBy(comparator), logger);
        }
        logger.println(format("Warning: Unsupported Comparator %s dropping down to client side sequence functionality", comparator));
        return super.sortBy(comparator);
    }

    @Override
    public <S> S reduce(Callable2<? super S, ? super Record, S> callable) {
        if(query().sql().isSupported(callable)){
            return (S) queryable.query(sqlQuery.reduce(callable).parameterisedExpression()).next();
        }
        logger.println(format("Warning: Unsupported Callable2 %s dropping down to client side sequence functionality", callable));
        return super.reduce(callable);
    }

    @Override
    public Number size() {
        return fold(0, count());
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
