package com.googlecode.totallylazy.numbers;

public interface UnaryOperators<T extends Number> {
    Number negate(T value);

    Number increment(T value);

    Number decrement(T value);
}
