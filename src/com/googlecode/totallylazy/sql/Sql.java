package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.callables.AscendingComparator;
import com.googlecode.totallylazy.callables.DescendingComparator;
import com.googlecode.totallylazy.numbers.BetweenPredicate;
import com.googlecode.totallylazy.numbers.GreaterThanOrEqualToPredicate;
import com.googlecode.totallylazy.numbers.GreaterThanPredicate;
import com.googlecode.totallylazy.numbers.LessThanOrEqualToPredicate;
import com.googlecode.totallylazy.numbers.LessThanPredicate;
import com.googlecode.totallylazy.predicates.AndPredicate;
import com.googlecode.totallylazy.predicates.ContainsPredicate;
import com.googlecode.totallylazy.predicates.EndsWithPredicate;
import com.googlecode.totallylazy.predicates.EqualsPredicate;
import com.googlecode.totallylazy.predicates.InPredicate;
import com.googlecode.totallylazy.predicates.Not;
import com.googlecode.totallylazy.predicates.OrPredicate;
import com.googlecode.totallylazy.predicates.StartsWithPredicate;
import com.googlecode.totallylazy.predicates.WherePredicate;

import java.util.Comparator;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Sql {
    public static Pair<String, Sequence<Object>> whereClause(Sequence<Predicate<? super Record>> where) {
        if(where.isEmpty()) return pair("", empty());
        final Sequence<Pair<String, Sequence<Object>>> sqlAndValues = where.map(toSql());
        return pair("where " + sqlAndValues.map(Callables.<String>first()).toString(" "),  sqlAndValues.flatMap(values()));
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
            return toSql(((AscendingComparator) comparator).callable()).first() + " asc ";
        }
        if(comparator instanceof DescendingComparator){
            return toSql(((DescendingComparator) comparator).callable()).first() + " desc ";
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
            return pair("( " + pairs.map(Callables.first(String.class)).toString("and ") + " ) ", pairs.flatMap(values()));
        }
        if(predicate instanceof OrPredicate){
            OrPredicate andPredicate = (OrPredicate) predicate;
            final Sequence<Pair<String, Sequence<Object>>> pairs = sequence(andPredicate.predicates()).map(toSql());
            return pair("( " + pairs.map(Callables.first(String.class)).toString("or ") + " ) ", pairs.flatMap(values()));
        }
        if(predicate instanceof EqualsPredicate){
            return pair("= ? ", sequence(((EqualsPredicate) predicate).value()));
        }
        if(predicate instanceof Not){
            return pair("<> ? ", sequence(toSql(((Not) predicate).predicate()).second()));
        }
        if(predicate instanceof GreaterThanPredicate){
            return pair("> ? ", sequence((Object)((GreaterThanPredicate) predicate).value()));
        }
        if(predicate instanceof GreaterThanOrEqualToPredicate){
            return pair(">= ? ", sequence((Object)((GreaterThanOrEqualToPredicate) predicate).value()));
        }
        if(predicate instanceof LessThanPredicate){
            return pair("< ? ", sequence((Object)((LessThanPredicate) predicate).value()));
        }
        if(predicate instanceof LessThanOrEqualToPredicate){
            return pair("<= ? ", sequence((Object)((LessThanOrEqualToPredicate) predicate).value()));
        }
        if(predicate instanceof BetweenPredicate){
            BetweenPredicate betweenPredicate = (BetweenPredicate) predicate;
            return pair("between ? and ? ", sequence((Object) betweenPredicate.lower(), betweenPredicate.upper()));
        }
        if(predicate instanceof InPredicate){
            InPredicate inPredicate = (InPredicate) predicate;
            return pair(repeat("?").take((Integer) inPredicate.values().size()).toString("in (", ",", ")"), (Sequence<Object>) inPredicate.values());
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
