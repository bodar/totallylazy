package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Identity;

public class Gcd extends Function2<Number, Number, Number> implements Identity<Number> {
    @Override
    public Number call(Number x, Number y) throws Exception {
        return Numbers.gcd(x, y);
    }

    @Override
    public Number identity() {
        return 0;
    }
}
