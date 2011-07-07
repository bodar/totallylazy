package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Callable2;

public class Sum<T extends Number> implements Callable2<T, T, Number> {
    public Number call(T a, T b) {
        return Numbers.add(a, b);
    }
}
