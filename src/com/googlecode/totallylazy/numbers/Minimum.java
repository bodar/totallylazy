package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.CombinerFunction;
import com.googlecode.totallylazy.comparators.NullComparator;

public class Minimum extends CombinerFunction<Number> implements com.googlecode.totallylazy.comparators.Minimum<Number> {
    @Override
    public Number call(Number a, Number b) throws Exception {
        return NullComparator.compare(a, b, NullComparator.Direction.Up, Numbers.ascending()) > 0 ? b : a;
    }

    @Override
    public Number identityElement() {
        return Numbers.POSITIVE_INFINITY;
    }
}
