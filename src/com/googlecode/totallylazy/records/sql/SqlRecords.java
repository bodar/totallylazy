package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.*;
import com.googlecode.totallylazy.numbers.Numbers;
import com.googlecode.totallylazy.records.AbstractRecords;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Queryable;
import com.googlecode.totallylazy.records.Record;

import java.sql.*;
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
import static com.googlecode.totallylazy.records.sql.Sql.sql;

public class SqlRecords extends AbstractRecords implements Queryable {
    private final Connection connection;

    public SqlRecords(Connection connection) {
        this.connection = connection;
    }

    public RecordSequence get(Keyword recordName) {
        return new RecordSequence(this, Query.query(recordName));
    }

    private static final Map<Class, String> typeMap = new HashMap<Class, String>() {{
        put(String.class, "varchar(256)");
        put(Date.class, "timestamp");
        put(Integer.class, "integer");
    }};

    public void define(Keyword recordName, Keyword<?>... fields) {
        try {
            final String sql = String.format("create table %s (%s)", recordName, sequence(fields).map(asColumn()));
            using(connection.createStatement(), executeUpdate(sql));
            Sql.LOGGER.log(Level.FINE, String.format("SQL:'%s'", sql));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Callable1<? super Statement, Integer> executeUpdate(final String sql) {
        return new Callable1<Statement, Integer>() {
            public Integer call(Statement statement) throws Exception {
                return statement.executeUpdate(sql);
            }
        };
    }

    public static <T extends Statement, R> R using(T t, Callable1<? super T, R> callable) {
        try {
            return Callers.call(callable, t);
        } finally {
            try {
                t.close();
            } catch (SQLException e) {
                throw new LazyException(e);
            }
        }
    }

    private static Callable1<? super Keyword<?>, String> asColumn() {
        return new Callable1<Keyword<?>, String>() {
            public String call(Keyword<?> keyword) throws Exception {
                return String.format("%s %s", keyword, typeMap.get(keyword.forClass()));
            }
        };
    }

    public Number add(Keyword recordName, final Sequence<Keyword> fields, final Sequence<Record> records) {
        if (records.isEmpty()) {
            return 0;
        }
        try {
            final String sql = String.format("insert into %s (%s) values (%s)",
                    recordName, fields, repeat("?").take((Integer) fields.size()));
            Number rowCount = using(connection.prepareStatement(sql), new Callable1<PreparedStatement, Number>() {
                public Number call(PreparedStatement statement) throws Exception {
                    for (Record record : records) {
                        addValues(statement, record.getValuesFor(fields));
                        statement.addBatch();
                    }
                    return numbers(statement.executeBatch()).reduce(Numbers.add());
                }
            });
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

    public Number update(String expression, final Sequence<?> parameters) {
        try {
            Number rowCount = using(connection.prepareStatement(expression), new Callable1<PreparedStatement, Number>() {
                public Number call(PreparedStatement statement) throws Exception {
                    addValues(statement, parameters);
                    return statement.executeUpdate();
                }
            });
            Sql.LOGGER.log(Level.FINE, String.format("SQL:'%s' Row Count: %s", expression, rowCount));
            return rowCount;
        } catch (SQLException e) {
            throw new LazyException(e);
        }

    }

    public RecordIterator query(Pair<String, Sequence<Object>> pair) {
        return query(pair.first(), pair.second());
    }

    public RecordIterator query(String expression, Sequence<?> parameters) {
        try {
            final PreparedStatement statement = connection.prepareStatement(expression);
            addValues(statement, parameters);
            String message = String.format("SQL:'%s' VALUES:'%s'", expression, parameters);
            Sql.LOGGER.log(Level.FINE, message);
            return new RecordIterator(statement);
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
}