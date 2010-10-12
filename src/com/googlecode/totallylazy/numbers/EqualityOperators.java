package com.googlecode.totallylazy.numbers;

public interface EqualityOperators {
    boolean isZero(Number x);

    boolean isPositive(Number x);

    boolean isNegative(Number x);

    boolean equalTo(Number x, Number y);

    boolean lessThan(Number x, Number y);
}
