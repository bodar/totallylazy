package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sets;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Queryable;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.SelectCallable;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

import static com.googlecode.totallylazy.Callables.ascending;
import static java.lang.String.format;

public class RecordSequence extends Sequence<Record> implements QuerySequence {
    private final Queryable queryable;
    private final Query query;
    private final PrintStream logger;

    public RecordSequence(final Queryable queryable, final Query query, final PrintStream logger) {
        this.queryable = queryable;
        this.query = query;
        this.logger = logger;
    }

    public Iterator<Record> iterator() {
        return execute(query);
    }

    private Iterator<Record> execute(final Query query) {
        return queryable.query(query.expressionAndParameters());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S> Sequence<S> map(final Callable1<? super Record, S> callable) {
        if (callable instanceof Keyword) {
            return new SingleValueSequence<S>(queryable, query.select((Keyword) callable), callable);
        }
        if (callable instanceof SelectCallable) {
            return (Sequence<S>) new RecordSequence(queryable, query.select(((SelectCallable) callable).keywords()), logger);
        }
        logger.println(format("Warning: unsupported callables %s dropping down to client side sequence functionality", callable));
        return super.map(callable);
    }

    @Override
    public Sequence<Record> filter(Predicate<? super Record> predicate) {
        if (query.sql().isSupported(predicate)) {
            return new RecordSequence(queryable, query.where(predicate), logger);
        }
        logger.println(format("Warning: Unsupported predicate %s dropping down to client side sequence functionality", predicate));
        return super.filter(predicate);
    }

    @Override
    public Sequence<Record> sortBy(Callable1<? super Record, ? extends Comparable> callable) {
        return sortBy(ascending(callable));
    }

    @Override
    public Sequence<Record> sortBy(Comparator<? super Record> comparator) {
        if (query.sql().isSupported(comparator)) {
            return new RecordSequence(queryable, query.orderBy(comparator), logger);
        }
        logger.println(format("Warning: unsupported comparator %s dropping down to client side sequence functionality", comparator));
        return super.sortBy(comparator);
    }

    @Override
    public Number size() {
        Record record = queryable.query(query.count().expressionAndParameters()).next();
        return (Number) record.fields().head().second();
    }

    @Override
    public <S extends Set<Record>> S toSet(S set) {
        return Sets.set(set, execute(query.distinct()));
    }

    @Override
    public Set<Record> toSet() {
        return Sets.set(execute(query.distinct()));
    }

    @Override
    public String toString() {
        return query.toString();
    }

    public Query query() {
        return query;
    }
}
