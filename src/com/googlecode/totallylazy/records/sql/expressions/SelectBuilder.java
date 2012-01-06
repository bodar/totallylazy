package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.callables.CountNotNull;
import com.googlecode.totallylazy.records.Aggregate;
import com.googlecode.totallylazy.records.Aggregates;
import com.googlecode.totallylazy.records.ImmutableKeyword;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;

import java.util.Comparator;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.Aggregate.aggregate;
import static com.googlecode.totallylazy.records.Keywords.keyword;
import static com.googlecode.totallylazy.records.sql.expressions.SelectExpression.selectExpression;
import static com.googlecode.totallylazy.records.sql.expressions.SetQuantifier.ALL;
import static com.googlecode.totallylazy.records.sql.expressions.SetQuantifier.DISTINCT;

public class SelectBuilder implements Expressible, Callable<Expression> {
    public static final ImmutableKeyword<Object> STAR = keyword("*");
    public static final Sequence<Keyword> ALL_COLUMNS = Sequences.<Keyword>sequence(STAR);
    private final SetQuantifier setQuantifier;
    private final Sequence<Keyword> select;
    private final Keyword table;
    private final Option<Predicate<? super Record>> where;
    private final Option<Comparator<? super Record>> comparator;

    private SelectBuilder(SetQuantifier setQuantifier, Sequence<? extends Keyword> select, Keyword table, Option<Predicate<? super Record>> where, Option<Comparator<? super Record>> comparator) {
        this.setQuantifier = setQuantifier;
        this.select = select.isEmpty() ? ALL_COLUMNS : select.safeCast(Keyword.class);
        this.table = table;
        this.where = where;
        this.comparator = comparator;
    }

    public SelectExpression call() throws Exception {
        return build();
    }

    public SelectExpression express() {
        return build();
    }

    public SelectExpression build() {
        return selectExpression(setQuantifier, select, table, where, comparator);
    }

    @Override
    public String toString() {
        return build().toString();
    }

    public Sequence<Keyword> select() {
        return select;
    }

    public static SelectBuilder from(Keyword table) {
        return new SelectBuilder(ALL, ALL_COLUMNS, table, Option.<Predicate<? super Record>>none(), Option.<Comparator<? super Record>>none());
    }

    public SelectBuilder select(Keyword... columns) {
        return select(sequence(columns));
    }

    public SelectBuilder select(Sequence<Keyword> columns) {
        return new SelectBuilder(setQuantifier, columns, table, where, comparator);
    }

    public SelectBuilder where(Predicate<? super Record> predicate) {
        return new SelectBuilder(setQuantifier, select, table, Option.<Predicate<? super Record>>some(predicate), comparator);
    }

    public SelectBuilder orderBy(Comparator<? super Record> comparator) {
        return new SelectBuilder(setQuantifier, select, table, where, Option.<Comparator<? super Record>>some(comparator));
    }

    @SuppressWarnings("unchecked")
    public SelectBuilder count() {
        Callable2 count = CountNotNull.count();
        Sequence<Keyword> sequence = Sequences.<Keyword>sequence(aggregate(count, STAR).as(keyword("record_count")));
        return new SelectBuilder(setQuantifier, sequence, table, where, comparator);
    }

    public SelectBuilder distinct() {
        return new SelectBuilder(DISTINCT, select, table, where, comparator);
    }

    @SuppressWarnings("unchecked")
    public <S> SelectBuilder reduce(Callable2 callable) {
        if (callable instanceof Aggregates) {
            return new SelectBuilder(setQuantifier, ((Aggregates) callable).value(), table, where, comparator);
        }
        return new SelectBuilder(setQuantifier, sequence(Aggregate.aggregate(callable, select().head())), table, where, comparator);
    }
}
