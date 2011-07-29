package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.records.Keyword;

import static com.googlecode.totallylazy.records.sql.expressions.Expressions.expression;
import static java.lang.String.format;

public class FromClause {
    public static Expression fromClause(Keyword table) {
        return expression(format("from %s", table));
    }

}
