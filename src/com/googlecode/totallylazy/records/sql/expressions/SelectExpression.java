package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Aggregate;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.sql.Expression;
import com.googlecode.totallylazy.records.sql.SetQuantifier;
import com.googlecode.totallylazy.records.sql.Sql;

import java.util.Comparator;

import static com.googlecode.totallylazy.records.sql.Expression.expression;
import static com.googlecode.totallylazy.records.sql.Expression.join;
import static com.googlecode.totallylazy.records.sql.expressions.FromClause.fromClause;
import static com.googlecode.totallylazy.records.sql.expressions.OrderByClause.orderByClause;
import static com.googlecode.totallylazy.records.sql.expressions.WhereClause.whereClause;
import static java.lang.String.format;

public class SelectExpression {
    public static Expression toSql(final SetQuantifier setQuantifier, final Sequence<Keyword> select, final Keyword table, final Option<Predicate<? super Record>> where, final Option<Comparator<? super Record>> sort) {
        return join(
                querySpecification(setQuantifier, select),
                fromClause(table),
                whereClause(where),
                orderByClause(sort));
    }

    public static Expression selectList(final Sequence<Keyword> select) {
        Sequence<Expression> expressions = select.map(keywordToExpression());
        return expression(expressions.map(Callables.<String>first()).toString(", "), expressions.flatMap(Sql.values()));
    }

    public static Expression querySpecification(SetQuantifier setQuantifier, final Sequence<Keyword> select) {
        return expression(format("select %s", setQuantifier)).join(selectList(select));
    }

    public static Callable1<Keyword, Expression> keywordToExpression() {
        return new Callable1<Keyword, Expression>() {
            public Expression call(Keyword keyword) throws Exception {
                if (keyword instanceof Aggregate) {
                    return Sql.asSql((Aggregate) keyword);
                }
                return Sql.toSql(keyword);
            }
        };
    }

}
