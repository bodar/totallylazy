package com.googlecode.totallylazy.sql;

import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.predicates.Is;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Query {
    private final Keyword table;
    private final Sequence<Keyword> select;
    private final Sequence<Pair<Keyword, Is>> where;

    private Query(Keyword table, Sequence<Keyword> select, Sequence<Pair<Keyword, Is>> where) {
        this.table = table;
        this.select = select;
        this.where = where;
    }

    public static Query selectAll(Keyword keyword) {
        return query(keyword, Sequences.<Keyword>empty(), Sequences.<Pair<Keyword, Is>>empty());
    }

    @Override
    public String toString() {
        return String.format("select %s from %s %s", selectClause(), table, whereClause());
    }

    private Object selectClause() {
        return select.isEmpty() ? "*" : sequence(select);
    }

    public Query select(Keyword... columns){
        return new Query(table, sequence(columns), where);
    }

    public static Query query(Keyword table, Sequence<Keyword> select, Sequence<Pair<Keyword, Is>> where) {
        return new Query(table, select, where);
    }

    public Query where(Keyword column, Is is) {
        return new Query(table, select, sequence(pair(column, is)));
    }

    private String whereClause() {
        if (where.isEmpty()) return "";
        return where.foldLeft(new StringBuilder().append("where "), new Callable2<StringBuilder, Pair<Keyword, Is>, StringBuilder>() {
            public StringBuilder call(StringBuilder builder, Pair<Keyword, Is> pair) throws Exception {
                return builder.append(pair.first()).append("  = ").append(pair.second().value());
            }
        }).toString();
    }
}
