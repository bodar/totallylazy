package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Monoid;

public class Gcd implements Monoid<Number> {
    @Override
    public Number call(Number x, Number y) throws Exception {
        return Numbers.gcd(x, y);
    }

    @Override
    public Number identity() {
        return 0;
    }
}
