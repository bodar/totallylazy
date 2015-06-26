package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.functions.CurriedMonoid;

public class Product implements CurriedMonoid<Number> {
    public Number call(Number multiplicand, Number multiplier) throws Exception {
        return Numbers.multiply(multiplicand, multiplier);
    }

    @Override
    public Number identity() {
        return 1;
    }
}