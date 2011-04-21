package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.*;
import com.googlecode.totallylazy.records.CountNotNull;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.ParameterisedExpression;
import com.googlecode.totallylazy.records.Record;

import java.util.Comparator;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.sql.SetQuantifier.ALL;
import static com.googlecode.totallylazy.records.sql.SetQuantifier.DISTINCT;

public class SqlQuery {
    private static final Keyword All  = Keyword.keyword("*");
    private final SetQuantifier setQuantifier;
    private final Sequence<Keyword> select;
    private final Callable2<?, ?, ?> setFunction;
    private final Keyword table;
    private final Sequence<Predicate<? super Record>> where;
    private final Option<Comparator<? super Record>> comparator;

    private SqlQuery(SetQuantifier setQuantifier, Sequence<Keyword> select, Callable2<?, ?, ?> setFunction, Keyword table, Sequence<Predicate<? super Record>> where, Option<Comparator<? super Record>> comparator) {
        this.setQuantifier = setQuantifier;
        this.select = select;
        this.setFunction = setFunction;
        this.table = table;
        this.where = where;
        this.comparator = comparator;
    }

    @Override
    public String toString() {
        final ParameterisedExpression pair = parameterisedExpression();
        return String.format(String.format("SQL:'%s' VALUES:'%s'", pair.expression(), pair.parameters()));
    }

    public Sql sql() {
        return new Sql();
    }

    public Keyword table() {
        return table;
    }

    public ParameterisedExpression parameterisedExpression() {
        final Pair<String, Sequence<Object>> whereClause = sql().whereClause(where);
        String sql = String.format("select %s %s from %s %s %s", setQuantifier, selectClause(), table, whereClause.first(), sql().orderByClause(comparator));
        return new ParameterisedExpression(select, sql, whereClause.second());
    }

    private Object selectClause() {
        return applyFunction(sequence(select).toString("", ",", ""));
    }

    private Object applyFunction(String columns) {
        return setFunction == null ? columns : sql().toSql(setFunction, columns);
    }

    public static SqlQuery query(Keyword table, Sequence<Keyword> select, Callable2<?, ?, ?> selectFunction, Sequence<Predicate<? super Record>> where, Option<Comparator<? super Record>> comparator, final SetQuantifier setQuantifier) {
        return new SqlQuery(setQuantifier, select, selectFunction, table, where, comparator);
    }

    public static SqlQuery query(Keyword table) {
        return query(table, sequence(All), null, Sequences.<Predicate<? super Record>>empty(), Option.<Comparator<? super Record>>none(), ALL);
    }

    public SqlQuery select(Keyword... columns) {
        return select(sequence(columns));
    }

    public SqlQuery select(Sequence<Keyword> columns) {
        return query(table, columns, setFunction, where, comparator, setQuantifier);
    }

    public SqlQuery where(Predicate<? super Record> predicate) {
        return query(table, select, setFunction, where.add(predicate), comparator, setQuantifier);
    }

    public SqlQuery orderBy(Comparator<? super Record> comparator) {
        return query(table, select, setFunction, where, Option.<Comparator<? super Record>>some(comparator), setQuantifier);
    }

    public SqlQuery count() {
        return query(table, select, CountNotNull.count(), where, comparator, setQuantifier);
    }

    public SqlQuery distinct() {
        return query(table, select, setFunction, where, comparator, DISTINCT);
    }

    public <S> SqlQuery reduce(Callable2<? super S, ?, S> callable) {
        return query(table, select, callable, where, comparator, setQuantifier);
    }
}
