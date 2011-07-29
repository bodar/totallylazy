package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.comparators.AscendingComparator;
import com.googlecode.totallylazy.comparators.DescendingComparator;
import com.googlecode.totallylazy.records.Record;

import java.util.Comparator;

import static com.googlecode.totallylazy.records.sql.expressions.Expressions.expression;

public class OrderByClause {
    public static Expression orderByClause(Option<Comparator<? super Record>> comparator) {
        return comparator.map(new Callable1<Comparator<? super Record>, Expression>() {
            public Expression call(Comparator<? super Record> comparator) throws Exception {
                return orderByClause(comparator);
            }
        }).getOrElse(Expressions.empty());
    }

    public static Expression orderByClause(Comparator<? super Record> comparator) {
        return expression("order by").join(toSql(comparator));
    }

    public static Expression toSql(Comparator<? super Record> comparator) {
        if (comparator instanceof AscendingComparator) {
            return SelectList.derivedColumn(((AscendingComparator<? super Record, ?>) comparator).callable()).join(expression("asc"));
        }
        if (comparator instanceof DescendingComparator) {
            return SelectList.derivedColumn(((DescendingComparator<? super Record, ?>) comparator).callable()).join(expression("desc"));
        }
        throw new UnsupportedOperationException("Unsupported comparator " + comparator);
    }
}
