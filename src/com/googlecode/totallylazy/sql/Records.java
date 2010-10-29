package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.MemorisedSequence;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Callables.second;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.sql.Keyword.keyword;

public class Records {
    private final Connection connection;

    public Records(Connection connection) {
        this.connection = connection;
    }

    public QuerySequence query(Keyword recordName) {
        return new QuerySequence(connection, Query.query(recordName));
    }

    private static final Map<Class, String> typeMap = new HashMap<Class, String>(){{
       put(String.class, "varchar(256)");
       put(Integer.class, "integer");
    }};

    public void define(Keyword recordName, Keyword<?>... fields) {
        try {
            final String sql = String.format("create table %s (%s)", recordName, sequence(fields).map(asColumn()));
            connection.createStatement().executeUpdate(sql);
            System.out.println(String.format("SQL:'%s'", sql));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Callable1<? super Keyword<?>, String> asColumn() {
        return new Callable1<Keyword<?>, String>() {
            public String call(Keyword<?> keyword) throws Exception {
                return String.format("%s %s", keyword, typeMap.get(keyword.forClass()));
            }
        };
    }

    public int add(Keyword recordName, Record... recordsArray) {
        try {
            final Sequence<Record> records = sequence(recordsArray);
            final Record first = records.first();
            final String sql = String.format("insert into %s (%s) values (%s)",
                    recordName, first.fields().map(first(Keyword.class)), repeat("?").take((Integer) first.fields().size()));
            final PreparedStatement statement = connection.prepareStatement(sql);
            int rowCount = 0;
            for (Record record : records) {
                addValues(statement, record.fields().map(second()));
                rowCount += statement.executeUpdate();
            }
            System.out.println(String.format("SQL:'%s' Row Count: %s", sql, rowCount));
            return rowCount;
        } catch (SQLException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    static void addValues(PreparedStatement statement, Sequence<Object> values) throws SQLException {
        for (Pair<Integer, Object> numberAndValue : iterate(increment(), 1).safeCast(Integer.class).zip(values)) {
            statement.setObject(numberAndValue.first(), numberAndValue.second());
        }
    }
}