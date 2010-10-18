package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.GenericType;

interface Operators<T extends Number> extends UnaryOperators<T>, EqualityOperators<T>, ArithmeticOperators, GenericType<T> {
    int priority();
}
