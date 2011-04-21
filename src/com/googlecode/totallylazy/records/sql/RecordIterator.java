package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.iterators.StatefulIterator;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.sql.mappings.Mappings;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.Strings.equalIgnoringCase;
import static com.googlecode.totallylazy.callables.LazyCallable.lazy;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.records.Keyword.keyword;
import static com.googlecode.totallylazy.records.Keywords.name;
import static com.googlecode.totallylazy.records.MapRecord.record;

public class RecordIterator extends StatefulIterator<Record> implements Closeable {
    private final Sequence<Keyword> keywords;
    private final Mappings mappings;
    private final Callable<PreparedStatement> preparedStatement;
    private ResultSet resultSet;

    public RecordIterator(final Sequence<Keyword> keywords, final Mappings mappings, final Callable<PreparedStatement> preparedStatement) {
        this.keywords = keywords;
        this.mappings = mappings;
        this.preparedStatement = lazy(preparedStatement);
    }

    @Override
    protected Option<Record> getNext() throws Exception {
        ResultSet resultSet = getResultSet();
        boolean hasNext = resultSet.next();
        if (!hasNext) {
            close();
            return none();
        }

        final Record record = record();
        final ResultSetMetaData metaData = resultSet.getMetaData();
        for (Integer index : iterate(increment(), 1).take(metaData.getColumnCount()).safeCast(Integer.class)) {
            final String name = metaData.getColumnName(index);
            Keyword keyword = keywords.find(where(name(), equalIgnoringCase(name))).getOrElse(keyword(name));
            record.set(keyword, mappings.getValue(resultSet, index, keyword.forClass()));
        }

        return some(record);
    }

    public PreparedStatement preparedStatement() throws Exception {
        return preparedStatement.call();
    }

    private ResultSet getResultSet() throws Exception {
        if (resultSet == null) {
            resultSet = preparedStatement().executeQuery();
        }
        return resultSet;
    }

    public void close() throws IOException {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            preparedStatement().close();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
