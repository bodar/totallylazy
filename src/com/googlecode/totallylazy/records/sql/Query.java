package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.*;
import com.googlecode.totallylazy.records.CountNotNull;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;

import java.util.Comparator;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Query {
    private static final Keyword All  = Keyword.keyword("*", Integer.class);
    private final SetQuantifier setQuantifier;
    private final Sequence<Keyword> select;
    private final Callable2<?, ?, ?> setFunction;
    private final Keyword table;
    private final Sequence<Predicate<? super Record>> where;
    private final Option<Comparator<? super Record>> comparator;

    private Query(SetQuantifier setQuantifier, Sequence<Keyword> select, Callable2<?, ?, ?> setFunction, Keyword table, Sequence<Predicate<? super Record>> where, Option<Comparator<? super Record>> comparator) {
        this.setQuantifier = setQuantifier;
        this.select = select;
        this.setFunction = setFunction;
        this.table = table;
        this.where = where;
        this.comparator = comparator;
    }

    @Override
    public String toString() {
        final Pair<String, Sequence<Object>> pair = expressionAndParameters();
        return String.format(String.format("SQL:'%s' VALUES:'%s'", pair.first(), pair.second()));
    }

    public Sql sql() {
        return new Sql();
    }

    public Keyword table() {
        return table;
    }

    public Sequence<Keyword> select() {
        return select;
    }

    public Pair<String, Sequence<Object>> expressionAndParameters() {
        final Pair<String, Sequence<Object>> whereClause = sql().whereClause(where);
        String sql = String.format("select %s %s from %s %s %s", setQuantifier, selectClause(), table, whereClause.first(), sql().orderByClause(comparator));
        return pair(sql, whereClause.second());
    }

    private Object selectClause() {
        return applyFunction(select.isEmpty() ? "*" : sequence(select).toString("", ",", ""));
    }

    private Object applyFunction(String columns) {
        return setFunction == null ? columns : sql().toSql(setFunction, columns);
    }

    public static Query query(Keyword table, Sequence<Keyword> select, Callable2<?, ?, ?> selectFunction, Sequence<Predicate<? super Record>> where, Option<Comparator<? super Record>> comparator, final SetQuantifier setQuantifier) {
        return new Query(setQuantifier, select, selectFunction, table, where, comparator);
    }

    public static Query query(Keyword table) {
        return query(table, sequence(All), null, Sequences.<Predicate<? super Record>>empty(), Option.<Comparator<? super Record>>none(), SetQuantifier.All);
    }

    public Query select(Keyword... columns) {
        return select(sequence(columns));
    }

    public Query select(Sequence<Keyword> columns) {
        return query(table, columns, setFunction, where, comparator, setQuantifier);
    }

    public Query where(Predicate<? super Record> predicate) {
        return query(table, select, setFunction, where.add(predicate), comparator, setQuantifier);
    }

    public Query orderBy(Comparator<? super Record> comparator) {
        return query(table, select, setFunction, where, Option.<Comparator<? super Record>>some(comparator), setQuantifier);
    }

    public Query count() {
        return query(table, select, CountNotNull.count(), where, comparator, setQuantifier);
    }

    public Query distinct() {
        return query(table, select, setFunction, where, comparator, SetQuantifier.Distinct);
    }

    public <S> Query reduce(Callable2<? super S, ?, S> callable) {
        return query(table, select, callable, where, comparator, setQuantifier);
    }
}
