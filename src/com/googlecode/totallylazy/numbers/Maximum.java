package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.CombinerFunction;
import com.googlecode.totallylazy.comparators.NullComparator;

public class Maximum extends CombinerFunction<Number> implements com.googlecode.totallylazy.comparators.Maximum<Number> {
    @Override
    public Number call(Number a, Number b) throws Exception {
        return NullComparator.compare(a, b, NullComparator.Direction.Down, Numbers.ascending()) > 0 ? a : b;
    }

    @Override
    public Number identityElement() {
        return Numbers.NEGATIVE_INFINITY;
    }
}
