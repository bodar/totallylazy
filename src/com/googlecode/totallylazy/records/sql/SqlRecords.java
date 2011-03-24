package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.*;
import com.googlecode.totallylazy.numbers.Numbers;
import com.googlecode.totallylazy.records.AbstractRecords;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Queryable;
import com.googlecode.totallylazy.records.Record;

import java.io.PrintStream;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Arrays.list;
import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.Sequences.*;
import static com.googlecode.totallylazy.Streams.nullOutputStream;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.numbers.Numbers.numbers;
import static com.googlecode.totallylazy.records.sql.Sql.sql;
import static java.lang.String.format;

public class SqlRecords extends AbstractRecords implements Queryable {
    private final Connection connection;
    private final PrintStream logger;

    public SqlRecords(Connection connection, PrintStream logger) {
        this.connection = connection;
        this.logger = logger;
    }

    public SqlRecords(Connection connection) {
        this(connection, new PrintStream(nullOutputStream()));
    }

    public RecordSequence get(Keyword recordName) {
        return new RecordSequence(this, Query.query(recordName), logger);
    }

    private static final Map<Class, String> typeMap = new HashMap<Class, String>() {{
        put(String.class, "varchar(256)");
        put(Date.class, "timestamp");
        put(Integer.class, "integer");
    }};

    public void define(Keyword recordName, Keyword<?>... fields) {
        try {
            final String sql = format("create table %s (%s)", recordName, sequence(fields).map(asColumn()));
            using(connection.createStatement(), executeUpdate(sql));
            logger.println(format("SQL:'%s'", sql));
        } catch (SQLException e) {
            throw new LazyException(e);
        }
    }

    public static Callable1<? super Statement, Integer> executeUpdate(final String sql) {
        return new Callable1<Statement, Integer>() {
            public Integer call(Statement statement) throws Exception {
                return statement.executeUpdate(sql);
            }
        };
    }

    private static Callable1<? super Keyword<?>, String> asColumn() {
        return new Callable1<Keyword<?>, String>() {
            public String call(Keyword<?> keyword) throws Exception {
                return format("%s %s", keyword, typeMap.get(keyword.forClass()));
            }
        };
    }

    public Number add(Keyword recordName, final Sequence<Keyword> fields, final Sequence<Record> records) {
        if (records.isEmpty()) {
            return 0;
        }
        try {
            final String sql = format("insert into %s (%s) values (%s)",
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
            logger.println(format("SQL:'%s' Row Count: %s", sql, rowCount));
            return rowCount;
        } catch (SQLException e) {
            throw new LazyException(e);
        }
    }

    public Number set(Keyword recordName, Predicate<? super Record> predicate, Sequence<Keyword> fields, Record record) {
        Pair<String, Sequence<Object>> where = sql(recordName).toSql(predicate);
        final String sql = format("update %s set %s where %s",
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
            logger.println(format("SQL:'%s' Row Count: %s", expression, rowCount));
            return rowCount;
        } catch (SQLException e) {
            throw new LazyException(e);
        }

    }

    public RecordIterator query(final Query query) {
        return new RecordIterator(query, new Callable<PreparedStatement>() {
            public PreparedStatement call() throws Exception {
                Pair<String, Sequence<Object>> pair = query.expressionAndParameters();
                String expression = pair.first();
                Sequence<?> parameters = pair.second();
                final PreparedStatement statement = connection.prepareStatement(expression);
                addValues(statement, parameters);
                logger.println(format(format("SQL:'%s' VALUES:'%s'", expression, parameters)));
                return statement;
            }
        });
    }

    public Number remove(Keyword recordName, Predicate<? super Record> predicate) {
        Pair<String, Sequence<Object>> where = sql(recordName).toSql(predicate);
        final String sql = format("delete from %s where %s",
                recordName, where.first());
        return update(sql, where.second());
    }

    public Number remove(Keyword recordName) {
        return update(format("delete from %s", recordName));
    }

    static void addValues(PreparedStatement statement, Sequence<?> values) throws SQLException {
        for (Pair<Integer, ?> numberAndValue : iterate(increment(), 1).safeCast(Integer.class).zip(values)) {
            Integer index = numberAndValue.first();
            Object value = numberAndValue.second();
            if(value instanceof Date){
                Date date = (Date) value;
                statement.setTimestamp(index, new Timestamp(date.getTime()));
            } else {
                statement.setObject(index, value);
            }
        }
    }
}