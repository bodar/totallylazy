package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.iterators.StatefulIterator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Records {
    public static Sequence<Record> records(Connection connection, Keyword keyword) {
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("select * from " + keyword.toString());
            return sequence(new RecordIterator(resultSet));
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

    private static class RecordIterator extends StatefulIterator<Record> {
        private final ResultSet resultSet;

        public RecordIterator(ResultSet resultSet) {
            this.resultSet = resultSet;
        }

        @Override
        protected Option<Record> getNext() throws SQLException {
            boolean hasNext = resultSet.next();
            if (!hasNext) {
                return none();
            }
            Record record = new SqlRecord(resultSet);
            return some(record);
        }

    }

    private static class SqlRecord implements Record {
        private final ResultSet resultSet;

        public SqlRecord(ResultSet resultSet) {
            this.resultSet = resultSet;
        }

        public <T> T get(Keyword<T> keyword) throws SQLException {
            return (T) resultSet.getObject(keyword.toString());
        }

        @Override
        public String toString() {
            try {
                StringBuilder builder = new StringBuilder();
                ResultSetMetaData metaData = resultSet.getMetaData();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    builder.append(metaData.getColumnName(i)).append(":");
                    builder.append(resultSet.getObject(i)).append(", ");
                }
                return builder.toString();
            } catch (SQLException e) {
                throw new UnsupportedOperationException(e);
            }
        }
    }
}
