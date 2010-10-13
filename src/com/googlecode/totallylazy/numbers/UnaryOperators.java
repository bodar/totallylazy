package com.googlecode.totallylazy.numbers;

public interface UnaryOperators<T extends Number> {
    Number negate(T x);

    Number increment(T x);

    Number decrement(T x);
}
