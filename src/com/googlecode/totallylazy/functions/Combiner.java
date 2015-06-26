package com.googlecode.totallylazy.functions;

public interface Combiner<T,R> extends Reducer<T,R> {
    R combine(R a, R b) throws Exception;
}
