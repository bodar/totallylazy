package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.CurriedMonoid;

public class Lcm implements CurriedMonoid<Number> {
    @Override
    public Number call(Number x, Number y) throws Exception {
        return Numbers.lcm(x, y);
    }

    @Override
    public Number identity() {
        return 1;
    }
}