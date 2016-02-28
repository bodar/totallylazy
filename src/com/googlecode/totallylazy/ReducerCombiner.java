package com.googlecode.totallylazy;

public interface ReducerCombiner<T,R> extends Reducer<R, T> {
    R combine(R a, R b) throws Exception;
}
