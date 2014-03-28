package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Binary.constructors.binary;

public abstract class BinaryFunction<T> implements Function2<T, T, T>, Binary<T> {
    @Override
    public Unary<T> apply(T t) {
        return Function2.super.apply(t)::call;
    }

    @Override
    public BinaryFunction<T> flip() {
        return binary(Function2.super.flip());
    }
}
