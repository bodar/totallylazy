package com.googlecode.totallylazy.records.sql.expressions;

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
import com.googlecode.totallylazy.records.sql.SetQuantifier;

import java.util.Comparator;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.Aggregate.aggregate;
import static com.googlecode.totallylazy.records.Keyword.keyword;
import static com.googlecode.totallylazy.records.sql.SetQuantifier.ALL;
import static com.googlecode.totallylazy.records.sql.SetQuantifier.DISTINCT;

public class ExpressionBuilder implements Expressible, Callable<Expression> {
    private final SetQuantifier setQuantifier;
    private final Sequence<Keyword> select;
    private final Keyword table;
    private final Option<Predicate<? super Record>> where;
    private final Option<Comparator<? super Record>> comparator;

    private ExpressionBuilder(SetQuantifier setQuantifier, Sequence<Keyword> select, Keyword table, Option<Predicate<? super Record>> where, Option<Comparator<? super Record>> comparator) {
        this.setQuantifier = setQuantifier;
        this.select = select;
        this.table = table;
        this.where = where;
        this.comparator = comparator;
    }

    public Expression call() throws Exception {
        return build();
    }

    public Expression express() {
        return build();
    }

    public Expression build() {
        return SelectExpression.toSql(setQuantifier, select, table, where, comparator);
    }

    @Override
    public String toString() {
        final Expression expression = build();
        return String.format(String.format("SQL:'%s' VALUES:'%s'", expression.expression(), expression.parameters()));
    }

    public Sequence<Keyword> select() {
        return select;
    }

    public static ExpressionBuilder query(Keyword table, Sequence<Keyword> select, Option<Predicate<? super Record>> where, Option<Comparator<? super Record>> comparator, final SetQuantifier setQuantifier) {
        return new ExpressionBuilder(setQuantifier, select, table, where, comparator);
    }

    public static ExpressionBuilder query(Keyword table, Sequence<Keyword> fields) {
        return query(table, fields, Option.<Predicate<? super Record>>none(), Option.<Comparator<? super Record>>none(), ALL);
    }

    public ExpressionBuilder select(Keyword... columns) {
        return select(sequence(columns));
    }

    public ExpressionBuilder select(Sequence<Keyword> columns) {
        return query(table, columns, where, comparator, setQuantifier);
    }

    public ExpressionBuilder where(Predicate<? super Record> predicate) {
        return query(table, select, Option.<Predicate<? super Record>>some(predicate), comparator, setQuantifier);
    }

    public ExpressionBuilder orderBy(Comparator<? super Record> comparator) {
        return query(table, select, where, Option.<Comparator<? super Record>>some(comparator), setQuantifier);
    }

    public ExpressionBuilder count() {
        Callable2 count = CountNotNull.<Number>count();
        Sequence<Keyword> sequence = Sequences.<Keyword>sequence(aggregate(count, keyword("*")).as(keyword("record_count")));
        return query(table, sequence, where, comparator, setQuantifier);
    }

    public ExpressionBuilder distinct() {
        return query(table, select, where, comparator, DISTINCT);
    }

    public <S> ExpressionBuilder reduce(Callable2 callable) {
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
