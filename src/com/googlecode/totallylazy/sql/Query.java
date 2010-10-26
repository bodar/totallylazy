package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.numbers.GreaterThanOrEqualToPredicate;
import com.googlecode.totallylazy.numbers.GreaterThanPredicate;
import com.googlecode.totallylazy.numbers.LessThanOrEqualToPredicate;
import com.googlecode.totallylazy.numbers.LessThanPredicate;
import com.googlecode.totallylazy.predicates.AndPredicate;
import com.googlecode.totallylazy.predicates.EqualsPredicate;
import com.googlecode.totallylazy.predicates.Not;
import com.googlecode.totallylazy.predicates.OrPredicate;
import com.googlecode.totallylazy.predicates.WherePredicate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Query {
    private final Keyword table;
    private final Sequence<Keyword> select;
    private final Sequence<Predicate<? super Record>> where;

    private Query(Keyword table, Sequence<Keyword> select, Sequence<Predicate<? super Record>> where) {
        this.table = table;
        this.select = select;
        this.where = where;
    }

    @Override
    public String toString() {
        final Pair<String, Sequence<Object>> pair = sqlAndValues();
        return String.format(String.format("SQL:'%s' VALUES:'%s'", pair.first(), pair.second()));
    }

    private Pair<String, Sequence<Object>> sqlAndValues() {
        final Pair<String, Sequence<Object>> whereClause = whereClause();
        return pair(String.format("select %s from %s %s", selectClause(), table, whereClause.first()), whereClause.second());
    }

    private Object selectClause() {
        return select.isEmpty() ? "*" : sequence(select);
    }

    public static Query query(Keyword table, Sequence<Keyword> select, Sequence<Predicate<? super Record>> where) {
        return new Query(table, select, where);
    }

    public static Query query(Keyword table) {
        return query(table, Sequences.<Keyword>empty(), Sequences.<Predicate<? super Record>>empty());
    }

    public Query select(Keyword... columns){
        return query(table, sequence(columns), where);
    }

    public Query where(Predicate<? super Record> predicate) {
        return query(table, select, where.add(predicate));
    }

    private Pair<String, Sequence<Object>> whereClause() {
        if(where.isEmpty()) return pair("", empty());
        final Sequence<Pair<String, Sequence<Object>>> sqlAndValues = where.map(toSql());
        return pair("where " + sqlAndValues.map(Callables.<String>first()).toString(" "),  sqlAndValues.flatMap(values()));
    }

    public boolean isSupported(Predicate<? super Record> predicate) {
        try{
            toSql(predicate);
            return true;
        } catch( UnsupportedOperationException e){
            System.out.println(String.format("Warning: %s dropping down to client side sequence functionality", e.getMessage()));
            return false;
        }
    }

    public Pair<String, Sequence<Object>> toSql(Predicate predicate) {
        if(predicate instanceof WherePredicate){
            WherePredicate wherePredicate = (WherePredicate) predicate;
            final Pair<String, Sequence<Object>> pair = toSql(wherePredicate.predicate());
            return pair(toSql(wherePredicate.callable()).first() + " " + pair.first(), pair.second());
        }
        if(predicate instanceof AndPredicate){
            AndPredicate andPredicate = (AndPredicate) predicate;
            final Sequence<Pair<String, Sequence<Object>>> pairs = sequence(andPredicate.predicates()).map(toSql());
            return pair(pairs.map(Callables.first(String.class)).toString("and "), pairs.flatMap(values()));
        }
        if(predicate instanceof OrPredicate){
            OrPredicate andPredicate = (OrPredicate) predicate;
            final Sequence<Pair<String, Sequence<Object>>> pairs = sequence(andPredicate.predicates()).map(toSql());
            return pair(pairs.map(Callables.first(String.class)).toString("or "), pairs.flatMap(values()));
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
        throw new UnsupportedOperationException("Unsupported predicate " + predicate);
    }

    private Callable1<Pair<String, Sequence<Object>>, Iterable<Object>> values() {
        return new Callable1<Pair<String, Sequence<Object>>, Iterable<Object>>() {
            public Iterable<Object> call(Pair<String, Sequence<Object>> pair) throws Exception {
                return pair.second();
            }
        };
    }

    private Callable1<? super Predicate, Pair<String, Sequence<Object>>> toSql() {
        return new Callable1<Predicate, Pair<String, Sequence<Object>>>() {
            public Pair<String, Sequence<Object>> call(Predicate predicate) throws Exception {
                return toSql(predicate);
            }
        };
    }

    public <T> Pair<String, Sequence<Object>> toSql(Callable1<? super Record, T> callable) {
        if(callable instanceof Keyword){
            return pair(callable.toString(), empty());
        }
        if(callable instanceof KeywordsCallable){
            return pair(sequence(((KeywordsCallable) callable).keywords()).toString(), empty());
        }
        throw new UnsupportedOperationException("Unsupported callable " + callable);
    }


    public ResultSet execute(Connection connection) throws SQLException {
        final Pair<String, Sequence<Object>> sqlAndValues = sqlAndValues();
        final PreparedStatement statement = connection.prepareStatement(sqlAndValues.first());
        Records.addValues(statement, sqlAndValues.second());
        return statement.executeQuery();
    }
}
