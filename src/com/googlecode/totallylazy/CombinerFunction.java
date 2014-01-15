package com.googlecode.totallylazy;

public abstract class CombinerFunction<T> implements BinaryOperator<T>, Combiner<T> {
    @Override
    public T combine(T a, T b) throws Exception {
        return call(a, b);
    }
}
