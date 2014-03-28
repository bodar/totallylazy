package com.googlecode.totallylazy;

public interface Binary<T> extends Function2<T, T, T> {
    @Override
    default Unary<T> apply(T t) {
        return Function2.super.apply(t)::call;
    }

    @Override
    default Binary<T> flip() {
        return Function2.super.flip()::call;
    }
}