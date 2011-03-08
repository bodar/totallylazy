package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.iterators.StatefulIterator;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.callables.LazyCallable.lazy;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.records.Keyword.keyword;
import static com.googlecode.totallylazy.records.MapRecord.record;

public class RecordIterator extends StatefulIterator<Record> {
    private final PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public RecordIterator(final PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    @Override
    protected Option<Record> getNext() throws Exception {
        ResultSet resultSet = getResultSet();
        boolean hasNext = resultSet.next();
        if (!hasNext) {
            preparedStatement.close();
            return none();
        }

        final Record record = record();
        final ResultSetMetaData metaData = resultSet.getMetaData();
        for (Integer columnIndex : iterate(increment(), 1).take(metaData.getColumnCount()).safeCast(Integer.class)) {
            final String name = metaData.getColumnName(columnIndex);
            final Object value = resultSet.getObject(columnIndex);
            final Keyword keyword = keyword(name, value == null ? Object.class : value.getClass());
            record.set(keyword, value);
        }

        return some(record);
    }

    private ResultSet getResultSet() throws SQLException {
        if(resultSet == null){
            resultSet = preparedStatement.executeQuery();
        }
        return resultSet;
    }
}
