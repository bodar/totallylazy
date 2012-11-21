package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Associative;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Identity;

public class Lcm extends Function2<Number, Number, Number> implements Identity<Number>, Associative<Number> {
    @Override
    public Number call(Number x, Number y) throws Exception {
        return Numbers.lcm(x, y);
    }

    @Override
    public Number identity() {
        return 1;
    }
}