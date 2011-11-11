package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sets;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.SelectCallable;
import com.googlecode.totallylazy.records.sql.expressions.Expressible;
import com.googlecode.totallylazy.records.sql.expressions.Expression;
import com.googlecode.totallylazy.records.sql.expressions.SelectBuilder;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

import static java.lang.String.format;

public class RecordSequence extends Sequence<Record> implements Expressible {
    private final SqlRecords sqlRecords;
    private final SelectBuilder builder;
    private final PrintStream logger;

    public RecordSequence(final SqlRecords records, final SelectBuilder builder, final PrintStream logger) {
        this.sqlRecords = records;
        this.builder = builder;
        this.logger = logger;
    }

    public Iterator<Record> iterator() {
        return execute(builder);
    }

    private Iterator<Record> execute(final SelectBuilder builder) {
        return sqlRecords.iterator(builder.build(), builder.select());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S> Sequence<S> map(final Callable1<? super Record, S> callable) {
        if (callable instanceof Keyword) {
            return new SingleValueSequence<S>(sqlRecords, builder.select((Keyword) callable), callable, logger);
        }
        if (callable instanceof SelectCallable) {
            return (Sequence<S>) new RecordSequence(sqlRecords, builder.select(((SelectCallable) callable).keywords()), logger);
        }
        logger.println(format("Warning: Unsupported Callable1 %s dropping down to client side sequence functionality", callable));
        return super.map(callable);
    }

    @Override
    public Sequence<Record> filter(Predicate<? super Record> predicate) {
        try {
            return new RecordSequence(sqlRecords, builder.where(predicate), logger);
        } catch (UnsupportedOperationException ex) {
            logger.println(format("Warning: Unsupported Predicate %s dropping down to client side sequence functionality", predicate));
            return super.filter(predicate);
        }
    }

    @Override
    public Sequence<Record> sortBy(Comparator<? super Record> comparator) {
        try {
            return new RecordSequence(sqlRecords, builder.orderBy(comparator), logger);
        } catch (UnsupportedOperationException ex) {
            logger.println(format("Warning: Unsupported Comparator %s dropping down to client side sequence functionality", comparator));
            return super.sortBy(comparator);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S> S reduce(Callable2<? super S, ? super Record, S> callable) {
        try {
            SelectBuilder query = builder.reduce(callable);
            return (S) sqlRecords.query(query.build(), query.select()).head();
        } catch (UnsupportedOperationException ex) {
            logger.println(format("Warning: Unsupported Callable2 %s dropping down to client side sequence functionality", callable));
            return super.reduce(callable);
        }
    }

    @Override
    public Number size() {
        SelectBuilder count = builder.count();
        return (Number) sqlRecords.query(count.build(), count.select()).head().fields().head().second();
    }

    @Override
    public <S extends Set<Record>> S toSet(S set) {
        return Sets.set(set, execute(builder.distinct()));
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    public Expression express() {
        return builder.build();
    }
}
