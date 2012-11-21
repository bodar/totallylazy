package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Combiner;
import com.googlecode.totallylazy.Function2;

public class Sum extends Function2<Number, Number, Number> implements Combiner<Number> {
    public Number call(Number a, Number b) {
        return Numbers.add(a, b);
    }

    @Override
    public Number identity() {
        return 0;
    }
}
