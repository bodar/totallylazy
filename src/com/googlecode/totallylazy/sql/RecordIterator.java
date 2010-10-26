package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.iterators.StatefulIterator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.callables.LazyCallable.lazy;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.sql.Keyword.keyword;
import static com.googlecode.totallylazy.sql.MapRecord.record;

public class RecordIterator extends StatefulIterator<Record> {
    private final Callable<ResultSet> lazyResults;

    public RecordIterator(final Connection connection, final Query query) {
        this.lazyResults = lazy(new Callable<ResultSet>() {
            public ResultSet call() throws Exception {
                System.out.println("query = " + query);
                return query.execute(connection);
            }
        });
    }

    @Override
    protected Option<Record> getNext() throws Exception {
        ResultSet resultSet = lazyResults.call();
        boolean hasNext = resultSet.next();
        if (!hasNext) {
            return none();
        }

        final Record mapRecord = record();
        final ResultSetMetaData metaData = resultSet.getMetaData();
        final int count = metaData.getColumnCount();
        for (Integer columnIndex : iterate(increment(), 1).take(count).safeCast(Integer.class)) {
            final String name = metaData.getColumnName(columnIndex);
            final Object value = resultSet.getObject(columnIndex);
            final Keyword keyword = keyword(name, value == null ? Object.class : value.getClass());
            mapRecord.set(keyword, value);
        }

        return some(mapRecord);
    }
}
