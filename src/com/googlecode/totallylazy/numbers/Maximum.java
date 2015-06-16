package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Monoid;
import com.googlecode.totallylazy.comparators.NullComparator;

public class Maximum implements com.googlecode.totallylazy.comparators.Maximum<Number>, Monoid<Number> {
    @Override
    public Number call(Number a, Number b) throws Exception {
        return NullComparator.compare(a, b, NullComparator.Direction.Down, Numbers.ascending()) > 0 ? a : b;
    }

    @Override
    public Number identity() {
        return Numbers.NEGATIVE_INFINITY;
    }
}
