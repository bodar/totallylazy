package com.googlecode.totallylazy;

public abstract class CombinerFunction<T> implements Monoid<T>, CurriedBinaryFunction<T> {
    @Override
    public T combine(T a, T b) throws Exception {
        return call(a, b);
    }
}
