package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.numbers.Numbers;
import com.googlecode.totallylazy.records.AbstractRecords;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Queryable;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.sql.expressions.Expression;
import com.googlecode.totallylazy.records.sql.expressions.ExpressionBuilder;
import com.googlecode.totallylazy.records.sql.expressions.Expressions;
import com.googlecode.totallylazy.records.sql.expressions.WhereClause;
import com.googlecode.totallylazy.records.sql.mappings.Mappings;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Streams.nullOutputStream;
import static com.googlecode.totallylazy.numbers.Numbers.numbers;
import static com.googlecode.totallylazy.records.sql.expressions.Expressions.expression;
import static java.lang.String.format;

public class SqlRecords extends AbstractRecords implements Queryable<Expression> {
    private final Connection connection;
    private final PrintStream logger;
    private final Mappings mappings;

    public SqlRecords(Connection connection, Mappings mappings, PrintStream logger) {
        this.connection = connection;
        this.mappings = mappings;
        this.logger = logger;
    }

    public SqlRecords(Connection connection) {
        this(connection, new Mappings(), new PrintStream(nullOutputStream()));
    }

    public RecordSequence get(Keyword recordName) {
        return new RecordSequence(this, ExpressionBuilder.query(recordName, definitions(recordName)), logger);
    }


    public Sequence<Record> query(final Expression expression, final Sequence<Keyword> definitions) {
        return new Sequence<Record>() {
            public Iterator<Record> iterator() {
                return SqlRecords.this.iterator(expression, definitions);
            }
        };
    }

    public RecordIterator iterator(Expression expression, Sequence<Keyword> definitions) {
        return new RecordIterator(connection, mappings, expression, definitions, logger);
    }

    public void define(Keyword recordName, Keyword<?>... fields) {
        super.define(recordName, fields);
        if (exists(recordName)) {
            return;
        }
        try {
            final String sql = format("create table %s (%s)", recordName, sequence(fields).map(asColumn()));
            using(connection.createStatement(), executeUpdate(sql));
            logger.println(format("SQL:'%s'", sql));
        } catch (SQLException e) {
            throw new LazyException(e);
        }
    }

    public boolean exists(Keyword recordName) {
        try {
            using(connection.createStatement(), executeQuery("select 1 from " + recordName));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Callable1<? super Statement, ResultSet> executeQuery(final String sql) {
        return new Callable1<Statement, ResultSet>() {
            public ResultSet call(Statement statement) throws Exception {
                return statement.executeQuery(sql);
            }
        };
    }

    public static Callable1<? super Statement, Integer> executeUpdate(final String sql) {
        return new Callable1<Statement, Integer>() {
            public Integer call(Statement statement) throws Exception {
                return statement.executeUpdate(sql);
            }
        };
    }

    private Callable1<? super Keyword<?>, String> asColumn() {
        return new Callable1<Keyword<?>, String>() {
            public String call(Keyword<?> keyword) throws Exception {
                return format("%s %s", keyword, mappings.getType(keyword.forClass()));
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
                        mappings.addValues(statement, record.getValuesFor(fields));
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
        Expression where = WhereClause.toSql(predicate);
        String sql = format("update %s set %s where %s", recordName, fields.toString("", "=?,", "=?"), where.text());
        return update(Expressions.expression(sql, record.getValuesFor(fields).join(where.parameters())));
    }

    public Number update(final Expression expression) {
        try {
            Number rowCount = using(connection.prepareStatement(expression.text()), new Callable1<PreparedStatement, Number>() {
                public Number call(PreparedStatement statement) throws Exception {
                    mappings.addValues(statement, expression.parameters());
                    return statement.executeUpdate();
                }
            });
            logger.println(format("SQL:'%s' Row Count: %s", expression, rowCount));
            return rowCount;
        } catch (SQLException e) {
            throw new LazyException(e);
        }
    }

    public Number remove(Keyword recordName, Predicate<? super Record> predicate) {
        Expression where = WhereClause.toSql(predicate);
        final String sql = format("delete from %s where %s", recordName, where.text());
        return update(expression(sql, where.parameters()));
    }

    public Number remove(Keyword recordName) {
        if (!exists(recordName)) {
            return 0;
        }
        return update(expression(format("delete from %s", recordName)));
    }

}