package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;

import static com.googlecode.totallylazy.records.sql.expressions.Expressions.expression;
import static com.googlecode.totallylazy.records.sql.expressions.Expressions.textOnly;
import static com.googlecode.totallylazy.records.sql.expressions.WhereClause.whereClause;

public class UpdateStatement extends CompoundExpression {
    public UpdateStatement(Keyword recordName, Predicate<? super Record> predicate, Sequence<Keyword> fields, final Sequence<Object> values) {
        super(
                textOnly("update").join(textOnly(recordName)),
                textOnly("set").join(expression(fields.map(Strings.format("%s=?")).toString(), values)),
                whereClause(predicate)
                );
    }

    public static Expression updateStatement(Keyword recordName, Predicate<? super Record> predicate, Sequence<Keyword> fields, final Sequence<Object> values) {
        return new UpdateStatement(recordName, predicate, fields, values);
    }
}
