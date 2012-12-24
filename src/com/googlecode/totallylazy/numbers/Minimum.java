package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.CombinerFunction;

public class Minimum extends CombinerFunction<Number> implements com.googlecode.totallylazy.comparators.Minimum<Number> {
    @Override
    public Number call(Number a, Number b) throws Exception {
        return Numbers.compare(a, b) > 0 ? b : a;
    }

    @Override
    public Number identity() {
        return Numbers.POSITIVE_INFINITY;
    }
}