package com.googlecode.totallylazy;

public interface Combiner<T,R> extends Reducer<T,R> {
    R combine(R a, R b) throws Exception;
}
