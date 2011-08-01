package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.AbstractRecords;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Queryable;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.sql.expressions.Expression;
import com.googlecode.totallylazy.records.sql.expressions.SelectBuilder;
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
import static com.googlecode.totallylazy.Streams.nullOutputStream;
import static com.googlecode.totallylazy.numbers.Numbers.numbers;
import static com.googlecode.totallylazy.numbers.Numbers.sum;
import static com.googlecode.totallylazy.records.Keywords.keyword;
import static com.googlecode.totallylazy.records.sql.expressions.DeleteStatement.deleteStatement;
import static com.googlecode.totallylazy.records.sql.expressions.SelectBuilder.from;
import static com.googlecode.totallylazy.records.sql.expressions.TableDefinition.tableDefinition;
import static com.googlecode.totallylazy.records.sql.expressions.UpdateStatement.updateStatement;
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
        return new RecordSequence(this, SelectBuilder.from(recordName).select(definitions(recordName)), logger);
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
        update(tableDefinition(recordName, fields));
    }


    private static final Keyword<Integer> one = keyword("1", Integer.class);
    public boolean exists(Keyword recordName) {
        try {
            using(connection.createStatement(), executeQuery(from(recordName).select(one).build().text()));
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

    public Number add(Keyword recordName, final Sequence<Keyword> fields, final Sequence<Record> records) {
        if (records.isEmpty()) {
            return 0;
        }
        try {
            final String sql = format("insert into %s (%s) values (%s)",
                    recordName, fields, repeat("?").take((Integer) fields.size()));
            logger.print(format("SQL: %s", sql));
            Number rowCount = using(connection.prepareStatement(sql), new Callable1<PreparedStatement, Number>() {
                public Number call(PreparedStatement statement) throws Exception {
                    for (Record record : records) {
                        mappings.addValues(statement, record.getValuesFor(fields));
                        statement.addBatch();
                    }
                    return numbers(statement.executeBatch()).reduce(sum());
                }
            });
            logger.println(format(" Count:%s", rowCount));
            return rowCount;
        } catch (SQLException e) {
            throw new LazyException(e);
        }
    }

    public Number set(Keyword recordName, Predicate<? super Record> predicate, Sequence<Keyword> fields, Record record) {
        return update(updateStatement(recordName, predicate, fields, record));
    }

    public Number update(final Expression expression) {
        try {
            logger.print(format("SQL: %s", expression));
            Number rowCount = using(connection.prepareStatement(expression.text()), new Callable1<PreparedStatement, Number>() {
                public Number call(PreparedStatement statement) throws Exception {
                    mappings.addValues(statement, expression.parameters());
                    return statement.executeUpdate();
                }
            });
            logger.println(format(" Count:%s", rowCount));
            return rowCount;
        } catch (SQLException e) {
            throw new LazyException(e);
        }
    }

    public Number remove(Keyword recordName, Predicate<? super Record> predicate) {
        return update(deleteStatement(recordName, predicate));
    }

    public Number remove(Keyword recordName) {
        if (!exists(recordName)) {
            return 0;
        }
        return update(deleteStatement(recordName));
    }

}