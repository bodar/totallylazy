package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.numbers.Numbers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Callables.second;
import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.numbers.Numbers.numbers;

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

    public Number add(Keyword recordName, Record... records) {
        return add(recordName, sequence(records));
    }

    public Number add(Keyword recordName, Sequence<Record> records) {
        return add(recordName, records.first().fields().map(first(Keyword.class)).realise(), records);
    }

    private Number add(Keyword recordName, Sequence<Keyword> fields, Sequence<Record> records) {
        try {
            final String sql = String.format("insert into %s (%s) values (%s)",
                    recordName, fields, repeat("?").take((Integer) fields.size()));
            final PreparedStatement statement = connection.prepareStatement(sql);
            for (Record record : records) {
                addValues(statement, record.fields().map(second()));
                statement.addBatch();
            }
            Number rowCount = numbers(statement.executeBatch()).reduce(Numbers.add());
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