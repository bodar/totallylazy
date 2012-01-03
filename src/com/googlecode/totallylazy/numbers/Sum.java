package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Function2;

public class Sum<T extends Number> extends Function2<T, T, Number> {
    public Number call(T a, T b) {
        return Numbers.add(a, b);
    }
}
