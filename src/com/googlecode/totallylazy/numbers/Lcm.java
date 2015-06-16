package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Monoid;

public class Lcm implements Monoid<Number> {
    @Override
    public Number call(Number x, Number y) throws Exception {
        return Numbers.lcm(x, y);
    }

    @Override
    public Number identity() {
        return 1;
    }
}