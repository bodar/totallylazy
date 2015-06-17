package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Binary.constructors.binary;
import static com.googlecode.totallylazy.Unary.constructors.unary;

public abstract class BinaryFunction<T> extends Curried2<T, T, T> implements Binary<T> {
    @Override
    public UnaryFunction<T> apply(T t) {
        return unary(super.apply(t));
    }

    @Override
    public BinaryFunction<T> flip() {
        return binary(super.flip());
    }
}
