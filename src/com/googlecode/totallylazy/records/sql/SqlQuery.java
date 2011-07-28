package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.records.Aggregate;
import com.googlecode.totallylazy.records.Aggregates;
import com.googlecode.totallylazy.records.CountNotNull;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;

import java.util.Comparator;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.Aggregate.aggregate;
import static com.googlecode.totallylazy.records.Keyword.keyword;
import static com.googlecode.totallylazy.records.sql.SetQuantifier.ALL;
import static com.googlecode.totallylazy.records.sql.SetQuantifier.DISTINCT;

public class SqlQuery {
    private final SetQuantifier setQuantifier;
    private final Sequence<Keyword> select;
    private final Keyword table;
    private final Sequence<Predicate<? super Record>> where;
    private final Option<Comparator<? super Record>> comparator;

    private SqlQuery(SetQuantifier setQuantifier, Sequence<Keyword> select, Keyword table, Sequence<Predicate<? super Record>> where, Option<Comparator<? super Record>> comparator) {
        this.setQuantifier = setQuantifier;
        this.select = select;
        this.table = table;
        this.where = where;
        this.comparator = comparator;
    }

    @Override
    public String toString() {
        final Expression expression = expression();
        return String.format(String.format("SQL:'%s' VALUES:'%s'", expression.expression(), expression.parameters()));
    }

    public Sequence<Keyword> select() {
        return select;
    }

    public Keyword table() {
        return table;
    }

    public Expression expression() {
        return Sql.toSql(setQuantifier, select, table, where, comparator);
    }

    public static SqlQuery query(Keyword table, Sequence<Keyword> select, Sequence<Predicate<? super Record>> where, Option<Comparator<? super Record>> comparator, final SetQuantifier setQuantifier) {
        return new SqlQuery(setQuantifier, select, table, where, comparator);
    }

    public static SqlQuery query(Keyword table, Sequence<Keyword> fields) {
        return query(table, fields, Sequences.<Predicate<? super Record>>empty(), Option.<Comparator<? super Record>>none(), ALL);
    }

    public SqlQuery select(Keyword... columns) {
        return select(sequence(columns));
    }

    public SqlQuery select(Sequence<Keyword> columns) {
        return query(table, columns, where, comparator, setQuantifier);
    }

    public SqlQuery where(Predicate<? super Record> predicate) {
        return query(table, select, where.add(predicate), comparator, setQuantifier);
    }

    public SqlQuery orderBy(Comparator<? super Record> comparator) {
        return query(table, select, where, Option.<Comparator<? super Record>>some(comparator), setQuantifier);
    }

    public SqlQuery count() {
        Callable2 count = CountNotNull.<Number>count();
        Sequence<Keyword> sequence = Sequences.<Keyword>sequence(aggregate(count, keyword("*")).as(keyword("record_count")));
        return query(table, sequence, where, comparator, setQuantifier);
    }

    public SqlQuery distinct() {
        return query(table, select, where, comparator, DISTINCT);
    }

    public <S> SqlQuery reduce(Callable2 callable) {
        if(callable instanceof Aggregates){
            return query(table, extract(((Aggregates) callable).value()), where, comparator, setQuantifier);
        }
        return query(table, select, where, comparator, setQuantifier);
    }

    private Sequence<Keyword> extract(Iterable<Aggregate> value) {
        return sequence(value).map(new Callable1<Aggregate, Keyword>() {
            public Keyword call(Aggregate aggregate) throws Exception {
                return aggregate;
            }
        });
    }
}
