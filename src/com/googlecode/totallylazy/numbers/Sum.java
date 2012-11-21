package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.CombinerFunction;

public class Sum extends CombinerFunction<Number> {
    public Number call(Number a, Number b) {
        return Numbers.add(a, b);
    }

    @Override
    public Number identity() {
        return 0;
    }
}
