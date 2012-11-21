package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Combiner;
import com.googlecode.totallylazy.Function2;

public class Lcm extends Function2<Number, Number, Number> implements Combiner<Number> {
    @Override
    public Number call(Number x, Number y) throws Exception {
        return Numbers.lcm(x, y);
    }

    @Override
    public Number identity() {
        return 1;
    }
}