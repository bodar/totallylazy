package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.*;
import com.googlecode.totallylazy.comparators.AscendingComparator;
import com.googlecode.totallylazy.comparators.DescendingComparator;
import com.googlecode.totallylazy.numbers.Sum;
import com.googlecode.totallylazy.numbers.Average;
import com.googlecode.totallylazy.predicates.AndPredicate;
import com.googlecode.totallylazy.predicates.Between;
import com.googlecode.totallylazy.predicates.ContainsPredicate;
import com.googlecode.totallylazy.predicates.EndsWithPredicate;
import com.googlecode.totallylazy.predicates.EqualsPredicate;
import com.googlecode.totallylazy.predicates.GreaterThan;
import com.googlecode.totallylazy.predicates.GreaterThanOrEqualTo;
import com.googlecode.totallylazy.predicates.InPredicate;
import com.googlecode.totallylazy.predicates.LessThan;
import com.googlecode.totallylazy.predicates.LessThanOrEqualTo;
import com.googlecode.totallylazy.predicates.Not;
import com.googlecode.totallylazy.predicates.NotNullPredicate;
import com.googlecode.totallylazy.predicates.NullPredicate;
import com.googlecode.totallylazy.predicates.OrPredicate;
import com.googlecode.totallylazy.predicates.StartsWithPredicate;
import com.googlecode.totallylazy.predicates.WherePredicate;
import com.googlecode.totallylazy.records.*;

import java.util.Comparator;

import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.sql.Expression.expression;
import static com.googlecode.totallylazy.records.sql.Expression.join;
import static java.lang.String.format;

public class Sql {
    private Sql() {
    }

    @SuppressWarnings("unchecked")
    public static Expression whereClause(Sequence<Predicate<? super Record>> where) {
        if (where.isEmpty()) return Expression.empty();
        final Sequence<Expression> sqlAndValues = where.map(toSql());
        return expression("where " + sqlAndValues.map(first(String.class)).toString(" "), sqlAndValues.flatMap(values()));
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

    @SuppressWarnings("unchecked")
    public static Expression toSql(Predicate predicate) {
        if (predicate instanceof WherePredicate) {
            WherePredicate wherePredicate = (WherePredicate) predicate;
            final Expression pair = toSql(wherePredicate.predicate());
            return expression(toSql(wherePredicate.callable()).first() + " " + pair.first(), pair.second());
        }
        if (predicate instanceof AndPredicate) {
            AndPredicate andPredicate = (AndPredicate) predicate;
            final Sequence<Expression> pairs = sequence(andPredicate.predicates()).map(toSql());
            return expression("( " + pairs.map(first(String.class)).toString("and ") + " ) ", pairs.flatMap(values()));
        }
        if (predicate instanceof OrPredicate) {
            OrPredicate andPredicate = (OrPredicate) predicate;
            final Sequence<Expression> pairs = sequence(andPredicate.predicates()).map(toSql());
            return expression("( " + pairs.map(first(String.class)).toString("or ") + " ) ", pairs.flatMap(values()));
        }
        if (predicate instanceof NullPredicate) {
            return expression(" is null ");
        }
        if (predicate instanceof NotNullPredicate) {
            return expression(" is not null ");
        }
        if (predicate instanceof EqualsPredicate) {
            return expression("= ? ", getValue(predicate));
        }
        if (predicate instanceof Not) {
            return expression("<> ? ", sequence(toSql(((Not) predicate).predicate()).second()));
        }
        if (predicate instanceof GreaterThan) {
            return expression("> ? ", getValue(predicate));
        }
        if (predicate instanceof GreaterThanOrEqualTo) {
            return expression(">= ? ", getValue(predicate));
        }
        if (predicate instanceof LessThan) {
            return expression("< ? ", getValue(predicate));
        }
        if (predicate instanceof LessThanOrEqualTo) {
            return expression("<= ? ", getValue(predicate));
        }
        if (predicate instanceof Between) {
            Between between = (Between) predicate;
            return expression("between ? and ? ", sequence(between.lower(), between.upper()));
        }
        if (predicate instanceof InPredicate) {
            InPredicate inPredicate = (InPredicate) predicate;
            Sequence sequence = inPredicate.values();
            if (sequence instanceof QuerySequence) {
                Expression pair = ((QuerySequence) sequence).query().expression();
                return expression("in ( " + pair.expression() + ")", pair.parameters());
            }
            return expression(repeat("?").take((Integer) inPredicate.values().size()).toString("in (", ",", ")"), (Sequence<Object>) sequence);
        }
        if (predicate instanceof StartsWithPredicate) {
            return expression("like ? ", sequence((Object) (((StartsWithPredicate) predicate).value() + "%%")));
        }
        if (predicate instanceof EndsWithPredicate) {
            return expression("like ? ", sequence((Object) ("%%" + ((EndsWithPredicate) predicate).value())));
        }
        if (predicate instanceof ContainsPredicate) {
            return expression("like ? ", sequence((Object) ("%%" + ((ContainsPredicate) predicate).value() + "%%")));
        }
        throw new UnsupportedOperationException("Unsupported predicate " + predicate);
    }

    private static Sequence<Object> getValue(Predicate predicate) {
        return sequence(((Value) predicate).value());
    }

    public static Callable1<? super Expression, Iterable<Object>> values() {
        return new Callable1<Expression, Iterable<Object>>() {
            public Iterable<Object> call(Expression pair) throws Exception {
                return pair.second();
            }
        };
    }

    public static Callable1<? super Predicate, Expression> toSql() {
        return new Callable1<Predicate, Expression>() {
            public Expression call(Predicate predicate) throws Exception {
                return toSql(predicate);
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

    public static Expression toSql(final SetQuantifier setQuantifier, final Sequence<Keyword> select, final Keyword table, final Sequence<Predicate<? super Record>> where, final Option<Comparator<? super Record>> sort) {
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
