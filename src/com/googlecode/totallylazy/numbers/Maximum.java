package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Combiner;
import com.googlecode.totallylazy.Function2;

public class Maximum extends Function2<Number, Number, Number> implements Combiner<Number> {
    @Override
    public Number call(Number a, Number b) throws Exception {
        return Numbers.compare(a, b) > 0 ? a : b;
    }

    @Override
    public Number identity() {
        return Numbers.NEGATIVE_INFINITY;
    }
}
