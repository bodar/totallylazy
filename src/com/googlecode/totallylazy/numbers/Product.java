package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Associative;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Identity;

public class Product extends Function2<Number, Number, Number> implements Identity<Number>, Associative<Number> {
    public Number call(Number multiplicand, Number multiplier) throws Exception {
        return Numbers.multiply(multiplicand, multiplier);
    }

    @Override
    public Number identity() {
        return 1;
    }
}
