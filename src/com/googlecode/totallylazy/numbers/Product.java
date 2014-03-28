package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.CombinerFunction;

public class Product extends CombinerFunction<Number> {
    public Number call(Number multiplicand, Number multiplier) throws Exception {
        return Numbers.multiply(multiplicand, multiplier);
    }

    @Override
    public Number identityElement() {
        return 1;
    }
}