package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;

import static com.googlecode.totallylazy.records.sql.expressions.Expressions.textOnly;
import static com.googlecode.totallylazy.records.sql.expressions.FromClause.fromClause;
import static com.googlecode.totallylazy.records.sql.expressions.WhereClause.whereClause;

public class DeleteStatement extends CompoundExpression {
    public DeleteStatement(Keyword recordName, Option<Predicate<? super Record>> predicate) {
        super(
                textOnly("delete"),
                fromClause(recordName),
                whereClause(predicate)
                );
    }

    public static Expression deleteStatement(Keyword recordName, Predicate<? super Record> predicate) {
        return new DeleteStatement(recordName, Option.<Predicate<? super Record>>some(predicate));
    }

    public static Expression deleteStatement(Keyword recordName) {
        return new DeleteStatement(recordName, Option.<Predicate<? super Record>>none());
    }
}
