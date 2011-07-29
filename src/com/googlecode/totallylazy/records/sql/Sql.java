package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.*;
import com.googlecode.totallylazy.comparators.AscendingComparator;
import com.googlecode.totallylazy.comparators.DescendingComparator;
import com.googlecode.totallylazy.numbers.Sum;
import com.googlecode.totallylazy.numbers.Average;
import com.googlecode.totallylazy.records.*;
import com.googlecode.totallylazy.records.sql.expressions.WhereClause;

import java.util.Comparator;

import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.sql.Expression.expression;
import static com.googlecode.totallylazy.records.sql.Expression.join;
import static com.googlecode.totallylazy.records.sql.expressions.WhereClause.whereClause;
import static java.lang.String.format;

public class Sql {
    private Sql() {
    }

    public static Expression orderByClause(Option<Comparator<? super Record>> comparator) {
        return comparator.map(new Callable1<Comparator<? super Record>, Expression>() {
            public Expression call(Comparator<? super Record> comparator) throws Exception {
                return orderByClause(comparator);
            }
        }).getOrElse(Expression.empty());
    }

    public static Expression orderByClause(Comparator<? super Record> comparator) {
        return expression("order by ").join(toSql(comparator));
    }

    public static Expression toSql(Comparator<? super Record> comparator) {
        if (comparator instanceof AscendingComparator) {
            return toSql(((AscendingComparator<? super Record, ?>) comparator).callable()).join(expression(" asc "));
        }
        if (comparator instanceof DescendingComparator) {
            return toSql(((DescendingComparator<? super Record, ?>) comparator).callable()).join(expression(" desc "));
        }
        throw new UnsupportedOperationException("Unsupported comparator " + comparator);
    }

    public static <T> Expression toSql(Callable1<? super Record, T> callable) {
        if (callable instanceof Keyword) {
            return expression(callable.toString());
        }
        if (callable instanceof SelectCallable) {
            return expression(sequence(((SelectCallable) callable).keywords()).toString("", ",", ""));
        }
        throw new UnsupportedOperationException("Unsupported callable " + callable);
    }

    public static Callable1<? super Expression, Iterable<Object>> values() {
        return new Callable1<Expression, Iterable<Object>>() {
            public Iterable<Object> call(Expression pair) throws Exception {
                return pair.second();
            }
        };
    }

    public static Expression asSql(Aggregate aggregate) {
        return toSql(aggregate.callable(), aggregate.source().name()).join(expression(" as " + aggregate.name()));
    }

    private static Expression toSql(Callable2<?, ?, ?> callable, String column) {
        if(callable instanceof CountNotNull){
            return expression(format("count(%s)", column));
        }
        if(callable instanceof Average){
            return expression(format("avg(%s)", column));
        }
        if(callable instanceof Sum){
            return expression(format("sum(%s)", column));
        }
        if(callable instanceof Minimum){
            return expression(format("min(%s)", column));
        }
        if(callable instanceof Maximum){
            return expression(format("max(%s)", column));
        }
        throw new UnsupportedOperationException();
    }

    public static Expression selectList(final Sequence<Keyword> select) {
        Sequence<Expression> expressions = select.map(keywordToExpression());
        return expression(expressions.map(Callables.<String>first()).toString(", "), expressions.flatMap(values()));
    }

    public static Callable1<Keyword, Expression> keywordToExpression() {
        return new Callable1<Keyword, Expression>() {
            public Expression call(Keyword keyword) throws Exception {
                if(keyword instanceof Aggregate){
                    return asSql((Aggregate) keyword);
                }
                return toSql(keyword);
            }
        };
    }

    public static Expression toSql(final SetQuantifier setQuantifier, final Sequence<Keyword> select, final Keyword table, final Option<Predicate<? super Record>> where, final Option<Comparator<? super Record>> sort) {
        return join(
                querySpecification(setQuantifier, select),
                fromClause(table),
                whereClause(where),
                orderByClause(sort));
    }

    public static Expression querySpecification(SetQuantifier setQuantifier, final Sequence<Keyword> select) {
        return expression(format("select %s", setQuantifier)).join(selectList(select));
    }

    public static Expression fromClause(Keyword table) {
        return expression(format("from %s", table));
    }
}
