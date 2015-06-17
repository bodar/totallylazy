package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.CombinerFunction;

public class Lcm extends CombinerFunction<Number> {
    @Override
    public Number call(Number x, Number y) throws Exception {
        return Numbers.lcm(x, y);
    }

    @Override
    public Number identity() {
        return 1;
    }
}