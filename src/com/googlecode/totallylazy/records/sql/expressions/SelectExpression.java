package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;

import java.util.Comparator;

import static com.googlecode.totallylazy.records.sql.expressions.Expressions.expression;
import static com.googlecode.totallylazy.records.sql.expressions.FromClause.fromClause;
import static com.googlecode.totallylazy.records.sql.expressions.OrderByClause.orderByClause;
import static com.googlecode.totallylazy.records.sql.expressions.SelectList.selectList;
import static com.googlecode.totallylazy.records.sql.expressions.WhereClause.whereClause;
import static java.lang.String.format;

public class SelectExpression extends CompoundExpression {
    private SelectExpression(final SetQuantifier setQuantifier, final Sequence<Keyword> select, final Keyword table, final Option<Predicate<? super Record>> where, final Option<Comparator<? super Record>> sort) {
        super(
                querySpecification(setQuantifier, select),
                fromClause(table),
                whereClause(where),
                orderByClause(sort)
        );
    }

    public static SelectExpression toSql(final SetQuantifier setQuantifier, final Sequence<Keyword> select, final Keyword table, final Option<Predicate<? super Record>> where, final Option<Comparator<? super Record>> sort) {
        return new SelectExpression(setQuantifier, select, table, where, sort);
    }

    public static Expression querySpecification(SetQuantifier setQuantifier, final Sequence<Keyword> select) {
        return expression(format("select %s", setQuantifier)).join(selectList(select));
    }
}
