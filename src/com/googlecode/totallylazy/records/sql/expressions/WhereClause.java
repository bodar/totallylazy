package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Value;
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
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.sql.Sql;

import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.sql.expressions.Expressions.empty;
import static com.googlecode.totallylazy.records.sql.expressions.Expressions.expression;

public class WhereClause extends CompoundExpression{
    public WhereClause(Predicate<? super Record> predicate) {
        super(new TextOnlyExpression("where"), toSql(predicate));
    }

    public static WhereClause whereClause(Predicate<? super Record> predicate) {
        return new WhereClause(predicate);
    }

    public static Expression whereClause(Option<Predicate<? super Record>> predicate) {
        return predicate.map(new Callable1<Predicate<? super Record>, Expression>() {
            public Expression call(Predicate<? super Record> predicate) throws Exception {
                return whereClause(predicate);
            }
        }).getOrElse(empty());
    }

    @SuppressWarnings("unchecked")
    public static Expression toSql(Predicate predicate) {
        if (predicate instanceof WherePredicate) {
            WherePredicate wherePredicate = (WherePredicate) predicate;
            final Expression pair = toSql(wherePredicate.predicate());
            return expression(Sql.toSql(wherePredicate.callable()).text() + " " + pair.text(), pair.parameters());
        }
        if (predicate instanceof AndPredicate) {
            AndPredicate andPredicate = (AndPredicate) predicate;
            final Sequence<Expression> pairs = sequence(andPredicate.predicates()).map(toSql());
            return expression("( " + pairs.map(Expressions.text()).toString(" and ") + " )", pairs.flatMap(Expressions.parameters()));
        }
        if (predicate instanceof OrPredicate) {
            OrPredicate andPredicate = (OrPredicate) predicate;
            final Sequence<Expression> pairs = sequence(andPredicate.predicates()).map(toSql());
            return expression("( " + pairs.map(Expressions.text()).toString(" or ") + " )", pairs.flatMap(Expressions.parameters()));
        }
        if (predicate instanceof NullPredicate) {
            return expression("is null");
        }
        if (predicate instanceof NotNullPredicate) {
            return expression("is not null");
        }
        if (predicate instanceof EqualsPredicate) {
            return expression("= ?", getValue(predicate));
        }
        if (predicate instanceof Not) {
            return expression("<> ?", sequence(toSql(((Not) predicate).predicate()).parameters()));
        }
        if (predicate instanceof GreaterThan) {
            return expression("> ?", getValue(predicate));
        }
        if (predicate instanceof GreaterThanOrEqualTo) {
            return expression(">= ?", getValue(predicate));
        }
        if (predicate instanceof LessThan) {
            return expression("< ?", getValue(predicate));
        }
        if (predicate instanceof LessThanOrEqualTo) {
            return expression("<= ?", getValue(predicate));
        }
        if (predicate instanceof Between) {
            Between between = (Between) predicate;
            return expression("between ? and ?", sequence(between.lower(), between.upper()));
        }
        if (predicate instanceof InPredicate) {
            InPredicate inPredicate = (InPredicate) predicate;
            Sequence sequence = inPredicate.values();
            if (sequence instanceof Expressible) {
                Expression pair = ((Expressible) sequence).express();
                return expression("in ( " + pair.text() + ")", pair.parameters());
            }
            return expression(repeat("?").take((Integer) inPredicate.values().size()).toString("in (", ",", ")"), (Sequence<Object>) sequence);
        }
        if (predicate instanceof StartsWithPredicate) {
            return expression("like ?", sequence((Object) (((StartsWithPredicate) predicate).value() + "%%")));
        }
        if (predicate instanceof EndsWithPredicate) {
            return expression("like ?", sequence((Object) ("%%" + ((EndsWithPredicate) predicate).value())));
        }
        if (predicate instanceof ContainsPredicate) {
            return expression("like ?", sequence((Object) ("%%" + ((ContainsPredicate) predicate).value() + "%%")));
        }
        throw new UnsupportedOperationException("Unsupported predicate " + predicate);
    }

    private static Sequence<Object> getValue(Predicate predicate) {
        return sequence(((Value) predicate).value());
    }

    public static Callable1<? super Predicate, Expression> toSql() {
        return new Callable1<Predicate, Expression>() {
            public Expression call(Predicate predicate) throws Exception {
                return toSql(predicate);
            }
        };
    }

}
