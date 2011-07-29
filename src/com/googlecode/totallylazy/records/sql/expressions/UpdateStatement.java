package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;

import static com.googlecode.totallylazy.records.sql.expressions.Expressions.expression;
import static com.googlecode.totallylazy.records.sql.expressions.WhereClause.whereClause;
import static java.lang.String.format;

public class UpdateStatement extends CompoundExpression {
    public UpdateStatement(Keyword recordName, Predicate<? super Record> predicate, Sequence<Keyword> fields, Record record) {
        super(
                expression(format("update %s", recordName)),
                expression(format("set %s", fields.toString("", "=?,", "=?")), record.getValuesFor(fields)),
                whereClause(predicate)
                );
    }

    public static Expression updateStatement(Keyword recordName, Predicate<? super Record> predicate, Sequence<Keyword> fields, Record record) {
        return new UpdateStatement(recordName, predicate, fields, record);
    }
}
