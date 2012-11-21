package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Combiner;
import com.googlecode.totallylazy.Function2;

public class Product extends Function2<Number, Number, Number> implements Combiner<Number> {
    public Number call(Number multiplicand, Number multiplier) throws Exception {
        return Numbers.multiply(multiplicand, multiplier);
    }

    @Override
    public Number identity() {
        return 1;
    }
}
