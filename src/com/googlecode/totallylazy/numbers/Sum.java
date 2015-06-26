package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.functions.CurriedMonoid;

public class Sum implements CurriedMonoid<Number> {
    public Number call(Number a, Number b) {
        return Numbers.add(a, b);
    }

    @Override
    public Number identity() {
        return 0;
    }
}
