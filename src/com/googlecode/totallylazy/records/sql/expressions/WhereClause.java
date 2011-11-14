package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Value;
import com.googlecode.totallylazy.predicates.AllPredicate;
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

import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.sql.expressions.Expressions.empty;
import static com.googlecode.totallylazy.records.sql.expressions.Expressions.expression;
import static com.googlecode.totallylazy.records.sql.expressions.Expressions.textOnly;
import static com.googlecode.totallylazy.records.sql.expressions.SelectList.derivedColumn;

public class WhereClause extends CompoundExpression{
    public WhereClause(Predicate<? super Record> predicate) {
        super(new TextOnlyExpression("where"), toSql(predicate));
    }

    public static WhereClause whereClause(Predicate<? super Record> predicate) {
        return new WhereClause(predicate);
    }

    public static Expression whereClause(Option<Predicate<? super Record>> predicate) {
        return sequence(predicate).find(not(instanceOf(AllPredicate.class))).map(new Callable1<Predicate<? super Record>, Expression>() {
            public Expression call(Predicate<? super Record> predicate) throws Exception {
                return whereClause(predicate);
            }
        }).getOrElse(empty());
    }

    @SuppressWarnings("unchecked")
    public static Expression toSql(Predicate predicate) {
        if (predicate instanceof WherePredicate) {
            WherePredicate wherePredicate = (WherePredicate) predicate;
            return derivedColumn(wherePredicate.callable()).join(toSql(wherePredicate.predicate()));
        }
        if (predicate instanceof AndPredicate) {
            AndPredicate andPredicate = (AndPredicate) predicate;
            return Expressions.join(toExpressions(andPredicate.predicates()), "(", " and ", ")");
        }
        if (predicate instanceof OrPredicate) {
            OrPredicate andPredicate = (OrPredicate) predicate;
            return Expressions.join(toExpressions(andPredicate.predicates()), "(", " or ", ")");
        }
        if (predicate instanceof NullPredicate) {
            return textOnly("is null");
        }
        if (predicate instanceof NotNullPredicate) {
            return textOnly("is not null");
        }
        if (predicate instanceof EqualsPredicate) {
            return expression("= ?", getValue(predicate));
        }
        if (predicate instanceof Not) {
            return expression("!= ?", sequence(toSql(((Not) predicate).predicate()).parameters()));
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
            return expression("like ?", sequence((Object) (((StartsWithPredicate) predicate).value() + "%")));
        }
        if (predicate instanceof EndsWithPredicate) {
            return expression("like ?", sequence((Object) ("%" + ((EndsWithPredicate) predicate).value())));
        }
        if (predicate instanceof ContainsPredicate) {
            return expression("like ?", sequence((Object) ("%" + ((ContainsPredicate) predicate).value() + "%")));
        }
        throw new UnsupportedOperationException("Unsupported predicate " + predicate);
    }

    private static Sequence<Expression> toExpressions(Predicate[] predicates) {
        return sequence(predicates).map(toSql());
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
