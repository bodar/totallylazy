package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.iterators.StatefulIterator;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Keywords;
import com.googlecode.totallylazy.records.Record;

import java.sql.*;
import java.util.Date;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.Strings.equalIgnoringCase;
import static com.googlecode.totallylazy.callables.LazyCallable.lazy;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.records.Keyword.keyword;
import static com.googlecode.totallylazy.records.Keywords.name;
import static com.googlecode.totallylazy.records.MapRecord.record;

public class RecordIterator extends StatefulIterator<Record> {
    private final Callable<PreparedStatement> preparedStatement;
    private final Query query;
    private ResultSet resultSet;

    public RecordIterator(final Query query, final Callable<PreparedStatement> preparedStatement) {
        this.query = query;
        this.preparedStatement = lazy(preparedStatement);
    }

    @Override
    protected Option<Record> getNext() throws Exception {
        ResultSet resultSet = getResultSet();
        boolean hasNext = resultSet.next();
        if (!hasNext) {
            preparedStatement().close();
            return none();
        }

        final Record record = record();
        final ResultSetMetaData metaData = resultSet.getMetaData();
        for (Integer columnIndex : iterate(increment(), 1).take(metaData.getColumnCount()).safeCast(Integer.class)) {
            final String name = metaData.getColumnName(columnIndex);
            final Object value = getValue(resultSet, columnIndex, name);
            final Keyword keyword = keyword(name, value == null ? Object.class : value.getClass());
            record.set(keyword, value);
        }

        return some(record);
    }

    private Object getValue(ResultSet resultSet, Integer columnIndex, String name) throws SQLException {
        Sequence<Keyword> keywords = query.select();
        Option<Keyword> option = keywords.find(where(name(), equalIgnoringCase(name)));
        if(!option.isEmpty()){
            Keyword keyword = option.get();
            Class aClass = keyword.forClass();
            if(aClass.equals(Date.class)){
                return new Date(resultSet.getTimestamp(columnIndex).getTime());
            }
            if(aClass.equals(Timestamp.class)){
                return resultSet.getTimestamp(columnIndex);
            }
            if(aClass.equals(String.class)){
                return resultSet.getString(columnIndex);
            }
            if(aClass.equals(Integer.class)){
                if(resultSet.getObject(columnIndex) == null){
                    return null;
                }
                return resultSet.getInt(columnIndex);
            }
            if(aClass.equals(Long.class)){
                if(resultSet.getObject(columnIndex) == null){
                    return null;
                }
                return resultSet.getLong(columnIndex);
            }
        }
        return resultSet.getObject(columnIndex);
    }

    public PreparedStatement preparedStatement() throws Exception {
        return preparedStatement.call();
    }

    private ResultSet getResultSet() throws Exception {
        if(resultSet == null){
            resultSet = preparedStatement().executeQuery();
        }
        return resultSet;
    }
}
