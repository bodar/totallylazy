package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.iterators.StatefulIterator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Callables.second;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.sql.Keyword.keyword;
import static com.googlecode.totallylazy.sql.MapRecord.record;

public class Records {
    public static Sequence<Record> records(Connection connection, Keyword keyword) {
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("select * from " + keyword.toString());
            return sequence(new RecordIterator(keyword, resultSet)).memorise();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int define(Connection connection, Keyword<Object> recordName, Keyword<?>... fields) {
        try {
            return connection.createStatement().executeUpdate(String.format("create table %s (%s)", recordName, sequence(fields).map(asColumn())));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Callable1<? super Keyword<?>, String> asColumn() {
        return new Callable1<Keyword<?>, String>() {
            public String call(Keyword<?> keyword) throws Exception {
                return String.format("%s %s", keyword, keyword.forClass() == Integer.class ? "integer" : "varchar(20)");
            }
        };
    }

    public static void insert(Connection connection, Record... recordsArray) {
        try {
            final Sequence<Record> records = sequence(recordsArray);
            final Record first = records.first();
            final String sql = String.format("insert into %s (%s) values (%s)",
                    first.name(),
                    first.fields().map(first(Keyword.class)),
                    repeat("?").take((Integer) first.fields().size()));
            final PreparedStatement statement = connection.prepareStatement(sql);
            for (Record record : records) {
                for (Pair<Integer, Object> numberAndValue : iterate(increment(), 1).safeCast(Integer.class).zip(record.fields().map(second()))) {
                    statement.setObject(numberAndValue.first(), numberAndValue.second());
                }
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    private static class RecordIterator extends StatefulIterator<Record> {
        private final Keyword keyword;
        private final ResultSet resultSet;

        public RecordIterator(Keyword keyword, ResultSet resultSet) {
            this.keyword = keyword;
            this.resultSet = resultSet;
        }

        @Override
        protected Option<Record> getNext() throws SQLException {
            boolean hasNext = resultSet.next();
            if (!hasNext) {
                return none();
            }

            final Record mapRecord = record(keyword);
            final ResultSetMetaData metaData = resultSet.getMetaData();
            final int count = metaData.getColumnCount();
            for (Integer columnIndex : iterate(increment(), 1).take(count).safeCast(Integer.class)) {

                final String name = metaData.getColumnName(columnIndex);
                final Object value = resultSet.getObject(columnIndex);
                final Keyword keyword = keyword(name, value.getClass());
                mapRecord.set(keyword, value);
            }

            return some(mapRecord);
        }

    }

}
