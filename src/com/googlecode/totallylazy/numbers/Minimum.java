package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Monoid;
import com.googlecode.totallylazy.comparators.NullComparator;

public class Minimum implements com.googlecode.totallylazy.comparators.Minimum<Number>, Monoid<Number> {
    @Override
    public Number call(Number a, Number b) throws Exception {
        return NullComparator.compare(a, b, NullComparator.Direction.Up, Numbers.ascending()) > 0 ? b : a;
    }

    @Override
    public Number identity() {
        return Numbers.POSITIVE_INFINITY;
    }
}
