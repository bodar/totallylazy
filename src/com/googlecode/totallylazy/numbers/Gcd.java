package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.CombinerFunction;

public class Gcd extends CombinerFunction<Number> {
    @Override
    public Number call(Number x, Number y) throws Exception {
        return Numbers.gcd(x, y);
    }

    @Override
    public Number identity() {
        return 0;
    }
}
