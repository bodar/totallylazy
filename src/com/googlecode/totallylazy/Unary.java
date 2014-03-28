package com.googlecode.totallylazy;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface Unary<T> extends Function<T, T>, UnaryOperator<T> {
}
