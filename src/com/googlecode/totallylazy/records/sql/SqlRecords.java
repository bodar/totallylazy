package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.numbers.Numbers;
import com.googlecode.totallylazy.records.AbstractRecords;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Callables.second;
import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.numbers.Numbers.numbers;

public class SqlRecords extends AbstractRecords {
    private final Connection connection;

    public SqlRecords(Connection connection) {
        this.connection = connection;
    }

    public RecordSequence query(Keyword recordName) {
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

    public Number add(Keyword recordName, Sequence<Keyword> fields, Sequence<Record> records) {
        if ((Integer) records.size() == 0) {
            return 0;
        }
        try {
            final String sql = String.format("insert into %s (%s) values (%s)",
                    recordName, fields, repeat("?").take((Integer) fields.size()));
            final PreparedStatement statement = connection.prepareStatement(sql);
            for (Record record : records) {
                addValues(statement, record.fields().filter(where(first(Keyword.class), is(in(fields)))).map(second()));
                statement.addBatch();
            }
            Number rowCount = numbers(statement.executeBatch()).reduce(Numbers.add());
            System.out.println(String.format("SQL:'%s' Row Count: %s", sql, rowCount));
            return rowCount;
        } catch (SQLException e) {
            throw new LazyException(e);
        }
    }

    public Number set(Keyword recordName, Pair<? extends Predicate<Record>, Record>... pairs) {
        try {
            Pair<? extends Predicate<Record>, Record> firstRecord = sequence(pairs).first();
            Sequence<Pair<Keyword, Object>> fields = firstRecord.second().fields();
            Pair<String, Sequence<Object>> where = new Sql(recordName).toSql(firstRecord.first());
            final String sql = String.format("update %s set %s where %s",
                    recordName, fields.map(first(Keyword.class)).toString("", "=?,", "=?"), where.first());
            final PreparedStatement statement = connection.prepareStatement(sql);
            for (Pair<? extends Predicate<Record>, Record> pair : pairs) {
                addValues(statement, pair.second().fields().map(second()).join(where.second()));
                statement.addBatch();
            }
            Number rowCount = numbers(statement.executeBatch()).reduce(Numbers.add());
            System.out.println(String.format("SQL:'%s' Row Count: %s", sql, rowCount));
            return rowCount;
        } catch (SQLException e) {
            throw new LazyException(e);
        }
    }

    static void addValues(PreparedStatement statement, Sequence<Object> values) throws SQLException {
        for (Pair<Integer, Object> numberAndValue : iterate(increment(), 1).safeCast(Integer.class).zip(values)) {
            statement.setObject(numberAndValue.first(), numberAndValue.second());
        }
    }
}