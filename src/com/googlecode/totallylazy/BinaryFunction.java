package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Binary.constructors.binary;
import static com.googlecode.totallylazy.UnaryOperator.constructors.unary;

public abstract class BinaryFunction<T> extends Function2<T, T, T> implements Binary<T> {
    @Override
    public UnaryOperator<T> apply(T t) {
        return unary(super.apply(t));
    }

    @Override
    public BinaryFunction<T> flip() {
        return binary(super.flip());
    }
}
