package com.googlecode.totallylazy.numbers;

public interface EqualityOperators<T extends Number>  {
    boolean isZero(T x);

    boolean isPositive(T x);

    boolean isNegative(T x);

    boolean equalTo(Number x, Number y);

    boolean lessThan(Number x, Number y);
}
