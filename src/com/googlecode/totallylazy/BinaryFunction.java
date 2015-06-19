package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Binary.binary;

public abstract class BinaryFunction<T> implements CurriedFunction2<T, T, T>, Binary<T> {
    @Override
    public UnaryFunction<T> apply(T t) {
        return CurriedFunction2.super.apply(t)::call;
    }

    @Override
    public BinaryFunction<T> flip() {
        return binary(CurriedFunction2.super.flip());
    }
}
