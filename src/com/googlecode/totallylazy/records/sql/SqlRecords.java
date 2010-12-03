package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.numbers.Numbers;
import com.googlecode.totallylazy.records.AbstractRecords;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;

import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.numbers.Numbers.numbers;
import static com.googlecode.totallylazy.records.sql.Query.query;
import static com.googlecode.totallylazy.records.sql.Sql.sql;

public class SqlRecords extends AbstractRecords {
    private final Connection connection;

    public SqlRecords(Connection connection) {
        this.connection = connection;
    }

    public RecordSequence get(Keyword recordName) {
        return new RecordSequence(Query.query(connection, recordName));
    }

    private static final Map<Class, String> typeMap = new HashMap<Class, String>() {{
        put(String.class, "varchar(256)");
        put(Date.class, "timestamp");
        put(Integer.class, "integer");
    }};

    public void define(Keyword recordName, Keyword<?>... fields) {
        try {
            final String sql = String.format("create table %s (%s)", recordName, sequence(fields).map(asColumn()));
            connection.createStatement().executeUpdate(sql);
            Sql.LOGGER.log(Level.FINE, String.format("SQL:'%s'", sql));
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

    public Number add(Keyword recordName, Sequence<Keyword> fields, Sequence<Record> records) {
        if (records.isEmpty()) {
            return 0;
        }
        try {
            final String sql = String.format("insert into %s (%s) values (%s)",
                    recordName, fields, repeat("?").take((Integer) fields.size()));
            final PreparedStatement statement = connection.prepareStatement(sql);
            for (Record record : records) {
                addValues(statement, record.getValuesFor(fields));
                statement.addBatch();
            }
            Number rowCount = numbers(statement.executeBatch()).reduce(Numbers.add());
            Sql.LOGGER.log(Level.FINE, String.format("SQL:'%s' Row Count: %s", sql, rowCount));
            return rowCount;
        } catch (SQLException e) {
            throw new LazyException(e);
        }
    }

    public Number set(Keyword recordName, Predicate<? super Record> predicate, Sequence<Keyword> fields, Record record) {
        Pair<String, Sequence<Object>> where = sql(recordName).toSql(predicate);
        final String sql = String.format("update %s set %s where %s",
                recordName, fields.toString("", "=?,", "=?"), where.first());
        return update(sql, record.getValuesFor(fields).join(where.second()));
    }

    public Number update(String expression, Object... parameters) {
        return update(expression, sequence(parameters));
    }

    public Number update(String expression, Sequence<?> parameters) {
        try {
            final PreparedStatement statement = connection.prepareStatement(expression);
            addValues(statement, parameters);
            Number rowCount = statement.executeUpdate();
            Sql.LOGGER.log(Level.FINE, String.format("SQL:'%s' Row Count: %s", expression, rowCount));
            return rowCount;
        } catch (SQLException e) {
            throw new LazyException(e);
        }

    }

    public Sequence<Record> query(String expression, Sequence<?> parameters) {
        try {
            final PreparedStatement statement = connection.prepareStatement(expression);
            addValues(statement, parameters);
            String message = String.format("SQL:'%s' VALUES:'%s'", expression, parameters);
            return Sequences.sequence(new RecordIterator(log(message, lazyExecute(statement))));
        } catch (SQLException e) {
            throw new LazyException(e);
        }
    }

    public Number remove(Keyword recordName, Predicate<? super Record> predicate) {
        Pair<String, Sequence<Object>> where = sql(recordName).toSql(predicate);
        final String sql = String.format("delete from %s where %s",
                recordName, where.first());
        return update(sql, where.second());
    }

    public Number remove(Keyword recordName) {
        return update(String.format("delete from %s", recordName));
    }

    static void addValues(PreparedStatement statement, Sequence<?> values) throws SQLException {
        for (Pair<Integer, ?> numberAndValue : iterate(increment(), 1).safeCast(Integer.class).zip(values)) {
            statement.setObject(numberAndValue.first(), numberAndValue.second());
        }
    }

    private <T> Callable<T> log(final String message, final Callable<T> callable) {
        return new Callable<T>() {
            public T call() throws Exception {
                Sql.LOGGER.log(Level.FINE, message);
                return callable.call();
            }
        };
    }

    private Callable<ResultSet> lazyExecute(final PreparedStatement statement) {
        return new Callable<ResultSet>() {
            public ResultSet call() throws Exception {
                return statement.executeQuery();
            }
        };
    }


}