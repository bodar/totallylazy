package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.sql.Expression;
import com.googlecode.totallylazy.records.sql.SetQuantifier;
import com.googlecode.totallylazy.records.sql.Sql;

import static com.googlecode.totallylazy.records.sql.Expression.expression;
import static java.lang.String.format;

public class FromClause {
    public static Expression fromClause(Keyword table) {
        return expression(format("from %s", table));
    }

}
