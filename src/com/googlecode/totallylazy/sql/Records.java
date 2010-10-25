package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.MemorisedSequence;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Callables.second;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.sql.Keyword.keyword;

public class Records {
    public static QuerySequence records(Connection connection, Keyword keyword) {
        return new QuerySequence(connection, Query.selectAll(keyword));
    }

    public static int define(Connection connection, Keyword recordName, Keyword<?>... fields) {
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

    public static void insert(Connection connection, Keyword recordName, Record... recordsArray) {
        try {
            final Sequence<Record> records = sequence(recordsArray);
            final Record first = records.first();
            final String sql = String.format("insert into %s (%s) values (%s)",
                    recordName, first.fields().map(first(Keyword.class)), repeat("?").take((Integer) first.fields().size()));
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
}