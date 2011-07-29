package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.records.Keyword;

import static java.lang.String.format;

public class FromClause extends TextOnlyExpression{
    public FromClause(Keyword table) {
        super(format("from %s", table));
    }

    public static Expression fromClause(Keyword table) {
        return new FromClause(table);
    }
}
