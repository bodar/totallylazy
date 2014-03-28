package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Combiner;

public class Product implements Combiner<Number> {
    public Number call(Number multiplicand, Number multiplier) throws Exception {
        return Numbers.multiply(multiplicand, multiplier);
    }

    @Override
    public Number identityElement() {
        return 1;
    }
}