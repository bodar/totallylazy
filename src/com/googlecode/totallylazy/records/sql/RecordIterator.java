package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.iterators.StatefulIterator;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.sql.expressions.Expression;
import com.googlecode.totallylazy.records.sql.mappings.Mappings;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static com.googlecode.totallylazy.records.MapRecord.record;
import static com.googlecode.totallylazy.records.RecordCallables.getKeyword;
import static java.lang.String.format;

public class RecordIterator extends StatefulIterator<Record> implements Closeable {
    private final Connection connection;
    private final Mappings mappings;
    private final Expression expression;
    private final Sequence<Keyword> definitions;
    private final PrintStream logger;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public RecordIterator(final Connection connection, final Mappings mappings, final Expression expression, final Sequence<Keyword> definitions, final PrintStream logger) {
        this.definitions = definitions;
        this.logger = logger;
        this.connection = connection;
        this.expression = expression;
        this.mappings = mappings;
    }

    @Override
    protected Record getNext() throws Exception {
        ResultSet resultSet = getResultSet();
        boolean hasNext = resultSet.next();
        if (!hasNext) {
            close();
            return finished();
        }

        final Record record = record();
        final ResultSetMetaData metaData = resultSet.getMetaData();
        for (Integer index : iterate(increment(), 1).take(metaData.getColumnCount()).safeCast(Integer.class)) {
            final String name = metaData.getColumnName(index);
            Keyword keyword = getKeyword(name, definitions);
            record.set(keyword, mappings.getValue(resultSet, index, keyword.forClass()));
        }

        return record;
    }

    private ResultSet getResultSet() throws Exception {
        if (resultSet == null) {
            resultSet = prepareStatement().executeQuery();
        }
        return resultSet;
    }

    private PreparedStatement prepareStatement() throws SQLException {
        if (preparedStatement == null) {
            logger.println(format("SQL: %s", expression));
            preparedStatement = connection.prepareStatement(expression.text());
            mappings.addValues(preparedStatement, expression.parameters());
        }
        return preparedStatement;
    }

    public void close() throws IOException {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
