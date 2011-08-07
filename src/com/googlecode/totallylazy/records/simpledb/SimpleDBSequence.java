package com.googlecode.totallylazy.records.simpledb;

import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Iterators;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.SelectCallable;
import com.googlecode.totallylazy.records.simpledb.mappings.Mappings;
import com.googlecode.totallylazy.records.sql.expressions.AbstractExpression;
import com.googlecode.totallylazy.records.sql.expressions.SelectBuilder;
import com.googlecode.totallylazy.records.sql.expressions.SelectExpression;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.Iterator;

import static com.googlecode.totallylazy.records.Keywords.keyword;
import static com.googlecode.totallylazy.records.lucene.Lucene.and;
import static java.lang.String.format;

public class SimpleDBSequence<T> extends Sequence<T> {
    private final AmazonSimpleDB sdb;
    private final SelectBuilder builder;
    private final Mappings mappings;
    private final Callable1<? super Item, T> itemToRecord;
    private final PrintStream logger;

    public SimpleDBSequence(AmazonSimpleDB sdb, SelectBuilder builder, Mappings mappings, Callable1<? super Item, T> itemToRecord, PrintStream logger) {
        this.sdb = sdb;
        this.builder = builder;
        this.mappings = mappings;
        this.itemToRecord = itemToRecord;
        this.logger = logger;
    }

    public Iterator<T> iterator() {
        return iterator(builder.express());
    }

    private Iterator<T> iterator(final AbstractExpression expression) {
        String selectExpression = expression.toString(new Callable1<Object, Object>() {
            public Object call(Object value) throws Exception {
                return mappings.toString(value.getClass(), value);
            }
        });
        logger.println("SimpleDB: " + selectExpression);
        SelectResult result = sdb.select(new SelectRequest(selectExpression, true));
        logger.println("NextToken: " + result.getNextToken());
        return Iterators.map(result.getItems().iterator(), itemToRecord);
    }

    @Override
    public Sequence<T> filter(Predicate<? super T> predicate) {
        return new SimpleDBSequence<T>(sdb, builder.where((Predicate) predicate), mappings, itemToRecord, logger);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S> Sequence<S> map(final Callable1<? super T, S> callable) {
        Callable1 raw = callable;
        if (raw instanceof Keyword) {
            final Keyword<S> keyword = (Keyword<S>) raw;
            return new SimpleDBSequence<S>(sdb, builder.select(keyword), mappings, itemToValue(keyword), logger);
        }
        if (raw instanceof SelectCallable) {
            return (Sequence<S>) new SimpleDBSequence(sdb, builder.select(((SelectCallable) raw).keywords()), mappings, itemToRecord, logger);
        }
        logger.println(format("Warning: Unsupported Callable1 %s dropping down to client side sequence functionality", callable));
        return super.map(callable);
    }

    private <S> Callable1<Item, S> itemToValue(final Keyword<S> keyword) {
        return new Callable1<Item, S>() {
            public S call(Item item) throws Exception {
                return ((Record) itemToRecord.call(item)).get(keyword);
            }
        };
    }

    @Override
    public Sequence<T> sortBy(Comparator<? super T> comparator) {
        try {
            return new SimpleDBSequence(sdb, builder.orderBy((Comparator) comparator), mappings, itemToRecord, logger);
        } catch (UnsupportedOperationException ex) {
            logger.println(format("Warning: Unsupported Comparator %s dropping down to client side sequence functionality", comparator));
            return super.sortBy(comparator);
        }
    }

//    @Override
//    public Number size() {
//        SelectBuilder count = builder.select(keyword("count(*)"));
//        Record next = (Record) iterator(count.express()).next();
//        return Numbers.valueOf(next.get(keyword("Count", String.class)));
//    }
}
