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
    public static final TextOnlyExpression UPDATE = textOnly("update");
    public static final TextOnlyExpression SET = textOnly("set");

    public UpdateStatement(Keyword recordName, Predicate<? super Record> predicate, Sequence<Keyword> fields, final Sequence<Object> values) {
        super(
                UPDATE.join(textOnly(recordName)),
                setClause(fields, values),
                whereClause(predicate)
                );
    }

    public static CompoundExpression setClause(Sequence<Keyword> fields, Sequence<Object> values) {
        return SET.join(expression(fields.map(Strings.format("%s=?")).toString(), values));
    }

    public static Expression updateStatement(Keyword recordName, Predicate<? super Record> predicate, Sequence<Keyword> fields, final Sequence<Object> values) {
        return new UpdateStatement(recordName, predicate, fields, values);
    }
}
