package com.googlecode.totallylazy.numbers;

public interface EqualityOperators<T extends Number>  {
    boolean isZero(T value);

    boolean isPositive(T value);

    boolean isNegative(T value);

    boolean equalTo(Number x, Number y);

    boolean lessThan(Number x, Number y);
}
