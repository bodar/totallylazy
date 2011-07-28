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
import static com.googlecode.totallylazy.records.ParameterisedExpression.expression;

public class Sql {
    private Sql() {
    }

    @SuppressWarnings("unchecked")
    public static ParameterisedExpression whereClause(Sequence<Predicate<? super Record>> where) {
        if (where.isEmpty()) return ParameterisedExpression.empty();
        final Sequence<ParameterisedExpression> sqlAndValues = where.map(toSql());
        return expression("where " + sqlAndValues.map(first(String.class)).toString(" "), sqlAndValues.flatMap(values()));
    }

    public static ParameterisedExpression orderByClause(Option<Comparator<? super Record>> comparator) {
        return comparator.map(new Callable1<Comparator<? super Record>, ParameterisedExpression>() {
            public ParameterisedExpression call(Comparator<? super Record> comparator) throws Exception {
                return orderByClause(comparator);
            }
        }).getOrElse(ParameterisedExpression.empty());
    }

    public static ParameterisedExpression orderByClause(Comparator<? super Record> comparator) {
        return expression("order by ").join(toSql(comparator));
    }

    public static ParameterisedExpression toSql(Comparator<? super Record> comparator) {
        if (comparator instanceof AscendingComparator) {
            return toSql(((AscendingComparator<? super Record, ?>) comparator).callable()).join(expression(" asc "));
        }
        if (comparator instanceof DescendingComparator) {
            return toSql(((DescendingComparator<? super Record, ?>) comparator).callable()).join(expression(" desc "));
        }
        throw new UnsupportedOperationException("Unsupported comparator " + comparator);
    }

    public static boolean isSupported(Predicate<? super Record> predicate) {
        try {
            toSql(predicate);
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }

    public static <T> ParameterisedExpression toSql(Callable1<? super Record, T> callable) {
        if (callable instanceof Keyword) {
            return expression(callable.toString(), Sequences.empty());
        }
        if (callable instanceof SelectCallable) {
            return expression(sequence(((SelectCallable) callable).keywords()).toString("", ",", ""), Sequences.empty());
        }
        throw new UnsupportedOperationException("Unsupported callable " + callable);
    }

    @SuppressWarnings("unchecked")
    public static ParameterisedExpression toSql(Predicate predicate) {
        if (predicate instanceof WherePredicate) {
            WherePredicate wherePredicate = (WherePredicate) predicate;
            final ParameterisedExpression pair = toSql(wherePredicate.predicate());
            return expression(toSql(wherePredicate.callable()).first() + " " + pair.first(), pair.second());
        }
        if (predicate instanceof AndPredicate) {
            AndPredicate andPredicate = (AndPredicate) predicate;
            final Sequence<ParameterisedExpression> pairs = sequence(andPredicate.predicates()).map(toSql());
            return expression("( " + pairs.map(first(String.class)).toString("and ") + " ) ", pairs.flatMap(values()));
        }
        if (predicate instanceof OrPredicate) {
            OrPredicate andPredicate = (OrPredicate) predicate;
            final Sequence<ParameterisedExpression> pairs = sequence(andPredicate.predicates()).map(toSql());
            return expression("( " + pairs.map(first(String.class)).toString("or ") + " ) ", pairs.flatMap(values()));
        }
        if (predicate instanceof NullPredicate) {
            return expression(" is null ", Sequences.<Object>empty());
        }
        if (predicate instanceof NotNullPredicate) {
            return expression(" is not null ", Sequences.<Object>empty());
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
                ParameterisedExpression pair = ((QuerySequence) sequence).query().parameterisedExpression();
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

    public static Callable1<? super ParameterisedExpression, Iterable<Object>> values() {
        return new Callable1<ParameterisedExpression, Iterable<Object>>() {
            public Iterable<Object> call(ParameterisedExpression pair) throws Exception {
                return pair.second();
            }
        };
    }

    public static Callable1<? super Predicate, ParameterisedExpression> toSql() {
        return new Callable1<Predicate, ParameterisedExpression>() {
            public ParameterisedExpression call(Predicate predicate) throws Exception {
                return toSql(predicate);
            }
        };
    }

    public static boolean isSupported(Comparator<? super Record> comparator) {
        try {
            toSql(comparator);
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }

    public static String asSql(Aggregates aggregates) {
        return sequence(aggregates.value()).map(new Callable1<Aggregate, String>() {
            public String call(Aggregate aggregate) throws Exception {
                return asSql(aggregate);
            }
        }).toString();
    }

    public static String asSql(Aggregate aggregate) {
        return toSql(aggregate.callable(), aggregate.source().name()) + " as " + aggregate.name();
    }

    public static boolean isSupported(Callable2<?, ?, ?> callable) {
        try{
            if(callable instanceof Aggregates){
                asSql((Aggregates) callable);
                return true;
            }
            return false;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }

    private static String toSql(Callable2<?, ?, ?> callable, String column) {
        if(callable instanceof CountNotNull){
            return String.format("count(%s)", column);
        }
        if(callable instanceof Average){
            return String.format("avg(%s)", column);
        }
        if(callable instanceof Sum){
            return String.format("sum(%s)", column);
        }
        if(callable instanceof Minimum){
            return String.format("min(%s)", column);
        }
        if(callable instanceof Maximum){
            return String.format("max(%s)", column);
        }
        throw new UnsupportedOperationException();
    }

    public static ParameterisedExpression selectClause(final Sequence<Keyword> select) {
        return expression(select.map(keywordToSql()).toString(", "));
    }

    public static Callable1<Keyword, String> keywordToSql() {
        return new Callable1<Keyword, String>() {
            public String call(Keyword keyword) throws Exception {
                if(keyword instanceof Aggregate){
                    return asSql((Aggregate) keyword);
                }
                return keyword.name();
            }
        };
    }

    public static ParameterisedExpression toSql(final SetQuantifier setQuantifier, final Sequence<Keyword> select, final Keyword table, final Sequence<Predicate<? super Record>> where, final Option<Comparator<? super Record>> sort) {
        final ParameterisedExpression selectClause = selectClause(select);
        final ParameterisedExpression whereClause = whereClause(where);
        final ParameterisedExpression orderByClause = orderByClause(sort);
        String sql = String.format("select %s %s from %s %s %s", setQuantifier, selectClause.first(), table, whereClause.first(), orderByClause.first());
        return new ParameterisedExpression(sql, Sequences.join(selectClause.second(), whereClause.second(), orderByClause.second()));
    }
}
