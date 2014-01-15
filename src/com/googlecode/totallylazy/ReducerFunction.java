package com.googlecode.totallylazy;

public interface ReducerFunction<T,R> extends BiFunction<R, T, R>, Reducer<T,R> {
}
