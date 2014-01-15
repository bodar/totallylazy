package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.UnaryOperator.constructors.unary;

public interface BinaryOperator<T> extends BiFunction<T, T, T>, java.util.function.BinaryOperator<T> {
    static <T> BinaryOperator<T> binary(final BiFunction<T, T, T> callable) {
        return callable::call;
    }

    @Override
    default UnaryOperator<T> apply(T t) {
        return unary(BiFunction.super.apply(t));
    }

    @Override
    default BinaryOperator<T> flip() {
        return binary(BiFunction.super.flip());
    }
}