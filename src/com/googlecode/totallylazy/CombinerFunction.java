package com.googlecode.totallylazy;

public abstract class CombinerFunction<T> extends BinaryFunction<T> implements Combiner<T> {
    @Override
    public T combine(T a, T b) throws Exception {
        return call(a, b);
    }
}
