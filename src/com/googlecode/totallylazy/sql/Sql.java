package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Value;
import com.googlecode.totallylazy.callables.AscendingComparator;
import com.googlecode.totallylazy.callables.DescendingComparator;
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
import com.googlecode.totallylazy.predicates.OrPredicate;
import com.googlecode.totallylazy.predicates.StartsWithPredicate;
import com.googlecode.totallylazy.predicates.WherePredicate;

import java.util.Comparator;

import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Sql {
    public static Pair<String, Sequence<Object>> whereClause(Sequence<Predicate<? super Record>> where) {
        if(where.isEmpty()) return pair("", empty());
        final Sequence<Pair<String, Sequence<Object>>> sqlAndValues = where.map(toSql());
        return pair("where " + sqlAndValues.map(first(String.class)).toString(" "),  sqlAndValues.flatMap(values()));
    }

    public static String orderByClause(Option<Comparator<? super Record>> comparator) {
        return comparator.map(new Callable1<Comparator<? super Record>, String>() {
            public String call(Comparator<? super Record> comparator) throws Exception {
                return "order by " + Sql.toSql(comparator);
            }
        }).getOrElse("");
    }
    

    public static <T> String toSql(Comparator<? super Record> comparator) {
        if(comparator instanceof AscendingComparator){
            return toSql(((AscendingComparator<? super Record>) comparator).callable()).first() + " asc ";
        }
        if(comparator instanceof DescendingComparator){
            return toSql(((DescendingComparator<? super Record>) comparator).callable()).first() + " desc ";
        }
        throw new UnsupportedOperationException("Unsupported comparator " + comparator);
    }


    public static boolean isSupported(Predicate<? super Record> predicate) {
        try{
            toSql(predicate);
            return true;
        } catch( UnsupportedOperationException e){
            System.out.println(String.format("Warning: %s dropping down to client side sequence functionality", e.getMessage()));
            return false;
        }
    }

    public static <T> Pair<String, Sequence<Object>> toSql(Callable1<? super Record, T> callable) {
        if(callable instanceof Keyword){
            return pair(callable.toString(), empty());
        }
        if(callable instanceof KeywordsCallable){
            return pair(sequence(((KeywordsCallable) callable).keywords()).toString(), empty());
        }
        throw new UnsupportedOperationException("Unsupported callable " + callable);
    }



    public static Pair<String, Sequence<Object>> toSql(Predicate predicate) {
        if(predicate instanceof WherePredicate){
            WherePredicate wherePredicate = (WherePredicate) predicate;
            final Pair<String, Sequence<Object>> pair = toSql(wherePredicate.predicate());
            return pair(toSql(wherePredicate.callable()).first() + " " + pair.first(), pair.second());
        }
        if(predicate instanceof AndPredicate){
            AndPredicate andPredicate = (AndPredicate) predicate;
            final Sequence<Pair<String, Sequence<Object>>> pairs = sequence(andPredicate.predicates()).map(toSql());
            return pair("( " + pairs.map(first(String.class)).toString("and ") + " ) ", pairs.flatMap(values()));
        }
        if(predicate instanceof OrPredicate){
            OrPredicate andPredicate = (OrPredicate) predicate;
            final Sequence<Pair<String, Sequence<Object>>> pairs = sequence(andPredicate.predicates()).map(toSql());
            return pair("( " + pairs.map(first(String.class)).toString("or ") + " ) ", pairs.flatMap(values()));
        }
        if(predicate instanceof EqualsPredicate){
            return pair("= ? ", getValue(predicate));
        }
        if(predicate instanceof Not){
            return pair("<> ? ", sequence(toSql(((Not) predicate).predicate()).second()));
        }
        if(predicate instanceof GreaterThan){
            return pair("> ? ", getValue(predicate));
        }
        if(predicate instanceof GreaterThanOrEqualTo){
            return pair(">= ? ", getValue(predicate));
        }
        if(predicate instanceof LessThan){
            return pair("< ? ", getValue(predicate));
        }
        if(predicate instanceof LessThanOrEqualTo){
            return pair("<= ? ", getValue(predicate));
        }
        if(predicate instanceof Between){
            Between between = (Between) predicate;
            return pair("between ? and ? ", sequence(between.lower(), between.upper()));
        }
        if(predicate instanceof InPredicate){
            InPredicate inPredicate = (InPredicate) predicate;
            Sequence sequence = inPredicate.values();
            if(sequence instanceof QuerySequence){
                Pair<String, Sequence<Object>> pair = ((QuerySequence) sequence).query().sqlAndValues();
                return pair("in ( " + pair.first() + ")", pair.second());
            }
            return pair(repeat("?").take((Integer) inPredicate.values().size()).toString("in (", ",", ")"), (Sequence<Object>) sequence);
        }
        if(predicate instanceof StartsWithPredicate){
            return pair("like ? ", sequence((Object)(((StartsWithPredicate) predicate).value() + "%%")));
        }
        if(predicate instanceof EndsWithPredicate){
            return pair("like ? ", sequence((Object)("%%" + ((EndsWithPredicate) predicate).value())));
        }
        if(predicate instanceof ContainsPredicate){
            return pair("like ? ", sequence((Object)("%%" + ((ContainsPredicate) predicate).value() + "%%")));
        }
        throw new UnsupportedOperationException("Unsupported predicate " + predicate);
    }

    private static Sequence<Object> getValue(Predicate predicate) {
        return sequence(((Value) predicate).value());
    }

    public static  Callable1<? super Pair<String, Sequence<Object>>, Iterable<?>> values() {
        return new Callable1<Pair<String, Sequence<Object>>, Iterable<?>>() {
            public Iterable<?> call(Pair<String, Sequence<Object>> pair) throws Exception {
                return pair.second();
            }
        };
    }

    public static  Callable1<? super Predicate, Pair<String, Sequence<Object>>> toSql() {
        return new Callable1<Predicate, Pair<String, Sequence<Object>>>() {
            public Pair<String, Sequence<Object>> call(Predicate predicate) throws Exception {
                return toSql(predicate);
            }
        };
    }

    public static <T> boolean isSupported(Comparator<? super Record> comparator) {
        try{
            toSql(comparator);
            return true;
        } catch( UnsupportedOperationException e){
            System.out.println(String.format("Warning: %s dropping down to client side sequence functionality", e.getMessage()));
            return false;
        }
    }
}
