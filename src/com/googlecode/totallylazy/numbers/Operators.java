package com.googlecode.totallylazy.numbers;

interface Operators<T extends Number> extends UnaryOperators<T>, EqualityOperators<T>, ArithmeticOperators {
    Class<T> forClass();

    int priority();
}
