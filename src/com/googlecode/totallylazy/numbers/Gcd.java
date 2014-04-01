package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Combiner;

public class Gcd implements Combiner<Number> {
    @Override
    public Number call(Number x, Number y) throws Exception {
        return Numbers.gcd(x, y);
    }

    @Override
    public Number identityElement() {
        return 0;
    }
}
