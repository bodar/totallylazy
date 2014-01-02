package com.googlecode.totallylazy;

public interface ReducerCombiner<T,R> extends Reducer<T,R> {
    R combine(R a, R b) throws Exception;
}
