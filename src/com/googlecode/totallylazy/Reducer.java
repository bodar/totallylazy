package com.googlecode.totallylazy;

public interface Reducer<T, R> extends BiFunction<R, T, R>, Identity<R> {
}
