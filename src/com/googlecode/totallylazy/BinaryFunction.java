package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.BinaryFunction.binary;
import static com.googlecode.totallylazy.UnaryFunction.unary;

public interface BinaryFunction<T> extends Function2<T, T, T> {
    static <T> BinaryFunction<T> binary(final Function2<T, T, T> callable) {
        return callable::call;
    }

    @Override
    default UnaryFunction<T> apply(T t) {
        return unary(Function2.super.apply(t));
    }

    @Override
    default BinaryFunction<T> flip() {
        return binary(Function2.super.flip());
    }

}