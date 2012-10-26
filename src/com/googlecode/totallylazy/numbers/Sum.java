package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Function2;

public class Sum extends Function2<Number, Number, Number> {
    public Number call(Number a, Number b) {
        return Numbers.add(a, b);
    }
}
